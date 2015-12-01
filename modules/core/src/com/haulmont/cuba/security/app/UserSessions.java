/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.inject.Inject;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User sessions distributed cache.
 *
 * @author krivopustov
 * @version $Id$
 */
@Component(UserSessionsAPI.NAME)
public final class UserSessions implements UserSessionsAPI, AppContext.Listener {

    public static final String NOT_RESTRICTED = "Not restricted";

    static class UserSessionInfo implements Serializable {
        private static final long serialVersionUID = -4834267718111570841L;

        final UserSession session;
        final long since;
        volatile long lastUsedTs; // set to 0 when propagating removal to cluster

        UserSessionInfo(UserSession session, long now) {
            this.session = session;
            this.since = now;
            this.lastUsedTs = now;
        }

        @Override
        public String toString() {
            return session + ", since: " + new Date(since) + ", lastUsed: " + new Date(lastUsedTs);
        }
    }

    private Logger log = LoggerFactory.getLogger(UserSessions.class);

    private Map<UUID, UserSessionInfo> cache = new ConcurrentHashMap<>();

    private volatile int expirationTimeout = 1800;

    private Metadata metadata;

    private ClusterManagerAPI clusterManager;

    private UserSession NO_USER_SESSION;

    private GlobalConfig globalConfig;

    private ServerConfig serverConfig;

    private byte[] bytes;

    private int count;

    @Inject
    private TimeSource timeSource;

    @Inject
    private UuidSource uuidSource;

    @Inject
    private ServerInfoAPI serverInfo;

    @Inject
    private Persistence persistence;

    @Inject
    private Resources resources;

    public UserSessions() {
    }

    @Inject
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;

        User noUser = metadata.create(User.class);
        noUser.setLogin("server");
        NO_USER_SESSION = new UserSession(
                UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf"), noUser,
                Collections.<Role>emptyList(), Locale.getDefault(), true) {
            @Override
            public UUID getId() {
                return AppContext.NO_USER_CONTEXT.getSessionId();
            }
        };
        AppContext.addListener(this);
    }

    @Inject
    public void setConfiguration(Configuration configuration) {
        globalConfig = configuration.getConfig(GlobalConfig.class);
        serverConfig = configuration.getConfig(ServerConfig.class);
        setExpirationTimeoutSec(serverConfig.getUserSessionExpirationTimeoutSec());
    }

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        this.clusterManager.addListener(
                UserSessionInfo.class,
                new ClusterListener<UserSessionInfo>() {

                    @Override
                    public void receive(UserSessionInfo message) {
                        UUID id = message.session.getId();
                        if (message.lastUsedTs == 0) {
                            cache.remove(id);
                        } else {
                            UserSessionInfo usi = cache.get(id);
                            if (usi == null || usi.lastUsedTs < message.lastUsedTs) {
                                cache.put(id, message);
                            }
                        }
                    }

                    @Override
                    public byte[] getState() {
                        if (cache.isEmpty())
                            return new byte[0];

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeInt(cache.size());
                            for (UserSessionInfo usi : cache.values()) {
                                oos.writeObject(usi);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return bos.toByteArray();
                    }

                    @Override
                    public void setState(byte[] state) {
                        if (state == null || state.length == 0)
                            return;

                        ByteArrayInputStream bis = new ByteArrayInputStream(state);
                        try {
                            ObjectInputStream ois = new ObjectInputStream(bis);
                            int size = ois.readInt();
                            for (int i = 0; i < size; i++) {
                                UserSessionInfo usi = (UserSessionInfo) ois.readObject();
                                receive(usi);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            log.error("Error receiving state", e);
                        }
                    }
                }
        );
    }

    @Override
    public void applicationStarted() {
        String encodedStr = resources.getResourceAsString(serverConfig.getLicensePath());
        if (encodedStr == null) {
            log.error("\n======================================================"
                    + "\nInvalid license path: " + serverConfig.getLicensePath()
                    + "\n======================================================");
        } else {
            Object[] objects;
            try {
                bytes = Base64.decodeBase64(encodedStr);
            } catch (Exception ignored) {
            }
            objects = decode();
            if (objects != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n======================================================");
                sb.append("\nCUBA platform license");
                sb.append("\nType: ").append(objects[0]);
                sb.append("\nLicense ID: ").append(objects[1]);
                sb.append("\nLicensed to: ").append(objects[2]);
                sb.append("\nApplication: ").append(objects[3]);
                sb.append("\nValid for CUBA platform releases up to: ").append(objects[4]);
                sb.append("\nMaximum number of sessions: ").append(((int) objects[5]) == 0 ? NOT_RESTRICTED : objects[5]);
                sb.append("\nMaximum number of entities: ").append(((int) objects[6]) == 0 ? NOT_RESTRICTED : objects[6]);
                sb.append("\n======================================================");
                log.warn(sb.toString());
            }
        }

        if (!globalConfig.getTestMode()) {
            Timer timer = new Timer(true);
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            if (AppContext.isStarted()) {
                                count = updateCurrentSessions(cache.values());
                            }
                        }
                    },
                    20000,
                    10000
            );
        }
    }

    @Override
    public void applicationStopped() {
        try {
            String serverId = serverInfo.getServerId();
            long now = timeSource.currentTimeMillis();

            DbTypeConverter types = persistence.getDbTypeConverter();
            Object tsObj = types.getSqlObject(new Date(now));
            int tsType = types.getSqlType(Date.class);
            Object falseObj = types.getSqlObject(Boolean.FALSE);
            int boolType = types.getSqlType(Boolean.class);

            QueryRunner runner = new QueryRunner(persistence.getDataSource());
            runner.update(
                    "update SYS_SERVER set UPDATE_TS = ?, IS_RUNNING = ?, DATA = null where NAME = ?",
                    new Object[]{tsObj, falseObj, serverId},
                    new int[]{tsType, boolType, Types.VARCHAR}
            );
        } catch (Exception e) {
            log.error("Unable to update SYS_SERVER: " + e);
        }
    }

    private Object[] decode() {
        try {
            BigInteger modulus = new BigInteger("18067575663987735326841242779464849963427753383058608867564135320738629147323691302736061591062052549416513716629888492493056820982621037983191693191253192501503395852012311582921940563059939819291206980413052487032632214809671460979641654935753772670912438930755064425437054092209398837918235045229999194411886527048670166121407791756890867519091491607028477744091266213650133388651959054937201410008848221420087836750419721605315470836864588770539438076268570475092652895671550938980095793057202388488186533973398706509535952619833352266143112184736108225487400680111445272274042274164391489296561006468854335469461");
            BigInteger exponent = new BigInteger("65537");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decoded = cipher.doFinal(bytes);
            String str = new String(decoded, StandardCharsets.UTF_8);
            String[] split = str.split("\\^");

            Object[] arr = new Object[7];
            if (split.length == 7) {
                arr[0] = split[0].trim();                   // type
                arr[1] = split[1].trim();                   // id
                arr[2] = split[2].trim();                   // to
                arr[3] = split[3].trim();                   // app
                arr[4] = split[4].trim();                   // platform date
                arr[5] = Integer.valueOf(split[5].trim());  // sessions
                arr[6] = Integer.valueOf(split[6].trim());  // entities
            } else if (split.length == 3) {
                arr[0] = "Commercial";                      // type
                arr[1] = "1400001";                         // id
                arr[2] = split[1].trim();                   // to
                arr[3] = NOT_RESTRICTED;                    // app
                arr[4] = NOT_RESTRICTED;                    // platform date
                arr[5] = Integer.valueOf(split[2].trim());  // sessions
                arr[6] = 0;                                 // entities
            } else
                throw new RuntimeException();

            if (!arr[3].equals(NOT_RESTRICTED)) {
                boolean found = false;
                String config = AppContext.getProperty("cuba.metadataConfig");
                String[] files = config.split("\\s");
                String file = files[files.length - 1];
                InputStream stream = resources.getResourceAsStream(file);
                if (stream == null) {
                    log.error("Cannot load '" + file + "' to check application name");
                    throw new RuntimeException();
                }
                try {
                    Document document = Dom4j.readDocument(stream);
                    Element root = document.getRootElement();
                    for (Element element : (List<Element>) root.elements("metadata-model")) {
                        if (arr[3].equals(element.attributeValue("root-package"))) {
                            found = true;
                            break;
                        }
                    }
                } finally {
                    IOUtils.closeQuietly(stream);
                }
                if (!found) {
                    log.error("\n======================================================"
                            + "\nInvalid license file at " + serverConfig.getLicensePath()
                            + "\napplication name mismatch"
                            + "\n======================================================");
                    return null;
                }
            }

            if (!arr[4].equals(NOT_RESTRICTED)) {
                InputStream stream = getClass().getResourceAsStream("/com/haulmont/cuba/core/global/release.timestamp");
                if (stream == null) {
                    log.error("Cannot load platform release timestamp");
                    throw new RuntimeException();
                }
                String relStr = IOUtils.toString(stream);
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date relDate = fmt.parse(relStr.substring(0, 10));
                Date licDate = fmt.parse((String) arr[4]);
                if (relDate.after(licDate)) {
                    log.error("\n======================================================"
                            + "\nInvalid license file at " + serverConfig.getLicensePath()
                            + "\nunsupported platform release (licensed for releases"
                            + "\nup to " + arr[4] + ", current release: " + relStr.substring(0, 10) + ")"
                            + "\n======================================================");
                    return null;
                }
            }

            if ((int)arr[6] != 0) {
                int count = 0;
                for (MetaModel metaModel : metadata.getSession().getModels()) {
                    String name = metaModel.getName();
                    if (!name.equals("com.haulmont.cuba")
                            && !name.equals("com.haulmont.reports")
                            && !name.equals("com.haulmont.workflow")
                            && !name.equals("com.haulmont.fts")
                            && !name.equals("com.haulmont.ccpayments")
                            && !name.equals("com.haulmont.bpmn"))
                    {
                        count += metaModel.getClasses().size();
                    }
                }
                if (count > (int)arr[6]) {
                    log.error("\n======================================================"
                            + "\nInvalid license file at " + serverConfig.getLicensePath()
                            + "\nmaximum number of entities exceeded "
                            + "\n(licensed: " + arr[6] + ", actual: " + count + ")"
                            + "\n======================================================");
                    return null;
                }
            }

            return arr;

        } catch (Exception e) {
            log.error("\n======================================================"
                    + "\nInvalid license data at " + serverConfig.getLicensePath()
                    + ("on".equals(System.getProperty("cuba.lcnsng.debug")) ? "\n" + ExceptionUtils.getFullStackTrace(e) : "")
                    + "\n======================================================");
            return null;
        }
    }

    @Override
    public void add(UserSession session) {
        UserSessionInfo usi = new UserSessionInfo(session, timeSource.currentTimeMillis());
        cache.put(session.getId(), usi);
        if (!session.isSystem())
            clusterManager.send(usi);
        Object[] objects = decode();
        if (objects != null) {
            int licensed = (int) objects[5];
            if (licensed != 0 && count > licensed) {
                LoggerFactory.getLogger("com.haulmont.cuba.security.app.LoginWorkerBean").warn(
                        String.format("Active sessions: %d, licensed: %d", count, licensed));
            }
        } else {
            LoggerFactory.getLogger("com.haulmont.cuba.security.app.LoginWorkerBean").error("Invalid license data");
        }
    }

    @Override
    public void remove(UserSession session) {
        UserSessionInfo usi = cache.remove(session.getId());

        if (!session.isSystem() && usi != null) {
            usi.lastUsedTs = 0;
            clusterManager.send(usi);
        }
    }

    @Override
    public UserSession get(UUID id, boolean propagate) {
        if (!AppContext.isStarted())
            return NO_USER_SESSION;

        UserSessionInfo usi = cache.get(id);
        if (usi != null) {
            usi.lastUsedTs = timeSource.currentTimestamp().getTime();
            if (propagate && !usi.session.isSystem()) {
                clusterManager.send(usi);
            }
            return usi.session;
        }
        return null;
    }

    @Override
    public void propagate(UUID id) {
        UserSessionInfo usi = cache.get(id);
        if (usi != null) {
            usi.lastUsedTs = timeSource.currentTimestamp().getTime();
            clusterManager.send(usi);
        }
    }

    @Override
    public int getExpirationTimeoutSec() {
        return expirationTimeout;
    }

    @Override
    public void setExpirationTimeoutSec(int value) {
        expirationTimeout = value;
    }

    @Override
    public Collection<UserSessionEntity> getUserSessionInfo() {
        ArrayList<UserSessionEntity> sessionInfoList = new ArrayList<>();
        for (UserSessionInfo nfo : cache.values()) {
            UserSessionEntity use = createUserSessionEntity(nfo.session, nfo.since, nfo.lastUsedTs);
            sessionInfoList.add(use);
        }
        return sessionInfoList;
    }

    @Override
    public Map<String, Object> getLicenseInfo() {
        Object[] objects = decode();
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("licenseType", objects != null ? objects[0] : "invalid data");
        info.put("licenseId", objects != null ? objects[1] : "invalid data");
        info.put("licensedTo", objects != null ? objects[2] : "invalid data");
        info.put("rootPackage", objects != null ? objects[3] : "invalid data");
        info.put("licensedReleaseDate", objects != null ? objects[4] : "invalid data");
        info.put("licensedSessions", objects != null ? objects[5] : -1);
        info.put("licensedEntities", objects != null ? objects[6] : -1);
        info.put("activeSessions", count);
        return info;
    }

    private UserSessionEntity createUserSessionEntity(UserSession session, long since, long lastUsedTs) {
        UserSessionEntity use = metadata.create(UserSessionEntity.class);
        use.setId(session.getId());
        use.setLogin(session.getUser().getLogin());
        use.setUserName(session.getUser().getName());
        use.setAddress(session.getAddress());
        use.setClientInfo(session.getClientInfo());
        Date currSince = timeSource.currentTimestamp();
        currSince.setTime(since);
        use.setSince(currSince);
        Date last = timeSource.currentTimestamp();
        last.setTime(lastUsedTs);
        use.setLastUsedTs(last);
        use.setSystem(session.isSystem());
        return use;
    }

    @Override
    public void killSession(UUID id){
        UserSessionInfo usi = cache.remove(id);

        if (usi != null) {
            usi.lastUsedTs = 0;
            clusterManager.send(usi);
        }
    }

    @Override
    public void processEviction() {
        if (!AppContext.isStarted())
            return;

        log.trace("Processing eviction");
        long now = timeSource.currentTimeMillis();
        for (Iterator<UserSessionInfo> it = cache.values().iterator(); it.hasNext();) {
            UserSessionInfo usi = it.next();
            if (now > (usi.lastUsedTs + expirationTimeout * 1000)) {
                it.remove();

                usi.lastUsedTs = 0;
                clusterManager.send(usi);
            }
        }
    }

    int updateCurrentSessions(Collection<UserSessionInfo> userSessionInfo) {
        try {
            StringBuilder sb = new StringBuilder();
            for (UserSessionInfo info : userSessionInfo) {
                sb.append(info.session.getId()).append("\n");
            }

            String serverId = serverInfo.getServerId();
            long now = timeSource.currentTimeMillis();

            DbTypeConverter types = persistence.getDbTypeConverter();
            Object tsObj = types.getSqlObject(new Date(now));
            int tsType = types.getSqlType(Date.class);
            Object trueObj = types.getSqlObject(Boolean.TRUE);
            int boolType = types.getSqlType(Boolean.class);

            QueryRunner runner = new QueryRunner(persistence.getDataSource());

            int updated = runner.update(
                    "update SYS_SERVER set UPDATE_TS = ?, IS_RUNNING = ?, DATA = ? where NAME = ?",
                    new Object[]{tsObj, trueObj, sb.toString(), serverId},
                    new int[]{tsType, boolType, Types.VARCHAR, Types.VARCHAR}
            );
            if (updated == 0) {
                Object id = types.getSqlObject(uuidSource.createUuid());
                int idType = types.getSqlType(UUID.class);
                runner.update(
                        "insert into SYS_SERVER (ID, CREATE_TS, UPDATE_TS, NAME, IS_RUNNING, DATA) " +
                        "values (?, ?, ?, ?, ?, ?)",
                        new Object[]{id, tsObj, tsObj, serverId, trueObj, sb.toString()},
                        new int[]{idType, tsType, tsType, Types.VARCHAR, boolType, Types.VARCHAR}
                );
            }
            return runner.query(
                    "select DATA from SYS_SERVER where IS_RUNNING = ? and UPDATE_TS > ?",
                    new Object[]{trueObj, types.getSqlObject(new Date(now - 30000))},
                    new int[] {boolType, tsType},
                    new ResultSetHandler<Integer>() {
                        @Override
                        public Integer handle(ResultSet rs) throws SQLException {
                            Set<UUID> set = new HashSet<>();
                            while (rs.next()) {
                                String data = rs.getString(1);
                                if (data != null) {
                                    String[] strings = data.split("\\s");
                                    for (String string : strings) {
                                        if (!StringUtils.isEmpty(string)) {
                                            set.add(UuidProvider.fromString(string));
                                        }
                                    }
                                }
                            }
                            return set.size();
                        }
                    }
            );
        } catch (Exception e) {
            log.error("Unable to update SYS_SERVER: " + e);
            return -1;
        }
    }
}