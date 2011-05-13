/*
 * Copyright 2010 IT Mill Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.terminal.gwt.widgetsetutils;

import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ClientCriterion;
import com.vaadin.terminal.Paintable;
import com.vaadin.ui.ClientWidget;

import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to collect widgetset related information from classpath.
 * Utility will seek all directories from classpaths, and jar files having
 * "Vaadin-Widgetsets" key in their manifest file.
 * <p>
 * Used by WidgetMapGenerator and ide tools to implement some monkey coding for
 * you.
 * <p>
 * Developer notice: If you end up reading this comment, I guess you have faced
 * a sluggish performance of widget compilation or unreliable detection of
 * components in your classpaths. The thing you might be able to do is to use
 * annotation processing tool like apt to generate the needed information. Then
 * either use that information in {@link com.vaadin.terminal.gwt.widgetsetutils.WidgetMapGenerator} or create the
 * appropriate monkey code for gwt directly in annotation processor and get rid
 * of {@link com.vaadin.terminal.gwt.widgetsetutils.WidgetMapGenerator}. Using annotation processor might be a good
 * idea when dropping Java 1.5 support (integrated to javac in 6).
 *
 */
public class ClassPathExplorer {

    private static Logger logger = Logger
            .getLogger("com.vaadin.terminal.gwt.widgetsetutils");

    private final static FileFilter DIRECTORIES_ONLY = new FileFilter() {
        public boolean accept(File f) {
            if (f.exists() && f.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }
    };

    private static List<String> rawClasspathEntries = getRawClasspathEntries();
    private static Map<Object, String> classpathLocations = getClasspathLocations(rawClasspathEntries);

    private ClassPathExplorer() {
    }

    /**
     * Finds server side widgets with {@link com.vaadin.ui.ClientWidget} annotation.
     */
    public static Collection<Class<? extends Paintable>> getPaintablesHavingWidgetAnnotation() {

        Collection<Class<? extends Paintable>> paintables = new HashSet<Class<? extends Paintable>>();
        Set<Object> keySet = classpathLocations.keySet();
        for (Object o : keySet) {
            searchForPaintables(o, classpathLocations.get(o), paintables);
        }
        return paintables;

    }

    public static Collection<Class<? extends AcceptCriterion>> getCriterion() {
        if (acceptCriterion.isEmpty()) {
            // accept criterion are searched as a side effect, normally after
            // paintable detection
            getPaintablesHavingWidgetAnnotation();
        }
        return acceptCriterion;
    }

    /**
     * Determine every URL location defined by the current classpath, and it's
     * associated package name.
     */
    private final static List<String> getRawClasspathEntries() {
        // try to keep the order of the classpath
        List<String> locations = new ArrayList<String>();

        String pathSep = System.getProperty("path.separator");
        String classpath = System.getProperty("java.class.path");

        if (classpath.startsWith("\"")) {
            classpath = classpath.substring(1);
        }
        if (classpath.endsWith("\"")) {
            classpath = classpath.substring(0, classpath.length() - 1);
        }

        logger.info("Classpath: " + classpath);

        String[] split = classpath.split(pathSep);
        for (int i = 0; i < split.length; i++) {
            String classpathEntry = split[i];
            if (acceptClassPathEntry(classpathEntry)) {
                locations.add(classpathEntry);
            }
        }

        return locations;
    }

    /**
     * Determine every URL location defined by the current classpath, and it's
     * associated package name.
     */
    private final static Map<Object, String> getClasspathLocations(
            List<String> rawClasspathEntries) {
        // try to keep the order of the classpath
        Map<Object, String> locations = new LinkedHashMap<Object, String>();
        for (String classpathEntry : rawClasspathEntries) {
            File file = new File(classpathEntry);
            include(null, file, locations);
        }
        return locations;
    }

    private static boolean acceptClassPathEntry(String classpathEntry) {
        if (!classpathEntry.endsWith(".jar")) {
            // accept all non jars (practically directories)
            return true;
        } else {
            // accepts jars that comply with vaadin-component packaging
            // convention (.vaadin. or vaadin- as distribution packages),
            if (classpathEntry.contains("vaadin-")
                    || classpathEntry.contains(".vaadin.")) {
                return true;
            } else {
                URL url;
                try {
                    url = new URL("file:"
                            + new File(classpathEntry).getCanonicalPath());
                    url = new URL("jar:" + url.toExternalForm() + "!/");
                    JarURLConnection conn = (JarURLConnection) url
                            .openConnection();
                    logger.fine(url.toString());
                    JarFile jarFile = conn.getJarFile();
                    Manifest manifest = jarFile.getManifest();
                    if (manifest != null) {
                        Attributes mainAttributes = manifest
                                .getMainAttributes();
                        if (mainAttributes.getValue("Vaadin-Widgetsets") != null) {
                            return true;
                        }
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return false;
            }
        }
    }

    /**
     * Recursively add subdirectories and jar files to classpathlocations
     *
     * @param name
     * @param file
     * @param locations
     */
    private final static void include(String name, File file,
            Map<Object, String> locations) {
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            // could be a JAR file
            includeJar(file, locations);
            return;
        }

        if (file.isHidden() || file.getPath().contains(File.separator + ".")) {
            return;
        }

        if (name == null) {
            name = "";
        } else {
            name += ".";
        }

        // add all directories recursively
        File[] dirs = file.listFiles(DIRECTORIES_ONLY);
        for (int i = 0; i < dirs.length; i++) {
            try {
                // add the present directory
                if (!dirs[i].isHidden()
                        && !dirs[i].getPath().contains(File.separator + ".")) {
                    locations.put(dirs[i], name + dirs[i].getName());
                }
            } catch (Exception ioe) {
                return;
            }
            include(name + dirs[i].getName(), dirs[i], locations);
        }
    }

    private static void includeJar(File file, Map<Object, String> locations) {
        try {
            URL url = new URL("file:" + file.getCanonicalPath());
            url = new URL("jar:" + url.toExternalForm() + "!/");
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            JarFile jarFile = conn.getJarFile();
            if (jarFile != null) {
//                locations.put(url, "");
                locations.put(jarFile, "");
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return;
        }

    }

    private final static void searchForPaintables(Object o,
            String packageName,
            Collection<Class<? extends Paintable>> paintables) {

        if (o instanceof File) {
            File directory = (File) o;

//            logger.log(Level.INFO, "\nDIR: " + directory.getAbsolutePath() + " Exists: " + directory.exists() + "\n");

            if (directory.exists() && !directory.isHidden()) {
                // Get the list of the files contained in the directory
                String[] files = directory.list();
                for (int i = 0; i < files.length; i++) {
                    // we are only interested in .class files
                    if (files[i].endsWith(".class")) {
                        // remove the .class extension
                        String classname = files[i].substring(0,
                                files[i].length() - 6);
                        classname = packageName + "." + classname;
//                        logger.log(Level.INFO, "CLASSNAME: " + classname);
                        tryToAdd(classname, paintables);
                    }
                }
            }
        } else if (o instanceof JarFile) {
            try {
                JarFile jarFile = (JarFile) o;
                Enumeration<JarEntry> e = jarFile.entries();
                while (e.hasMoreElements()) {
                    JarEntry entry = e.nextElement();
                    String entryname = entry.getName();
                    if (!entry.isDirectory()
                            && entryname.endsWith(".class")) {
                        String classname = entryname.substring(0, entryname
                                .length() - 6);
                        if (classname.startsWith("/")) {
                            classname = classname.substring(1);
                        }
                        classname = classname.replace('/', '.');
                        tryToAdd(classname, paintables);
                    }
                }
            } catch (Exception e) {
                logger.warning(e.toString());
            }
        }

    }

    // Hide possible errors, exceptions from static initializers from
    // classes we are inspecting
    private static PrintStream devnull = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            // NOP
        }
    });

    private static Set<Class<? extends AcceptCriterion>> acceptCriterion = new HashSet<Class<? extends AcceptCriterion>>();

    private static void tryToAdd(final String fullclassName,
            Collection<Class<? extends Paintable>> paintables) {
        try {
            PrintStream out = System.out;
            PrintStream err = System.err;
            System.setErr(devnull);
            System.setOut(devnull);

//            logger.log(Level.INFO, "ClassName: " +  fullclassName);

            Class<?> c = Class.forName(fullclassName);

            System.setErr(err);
            System.setOut(out);

            if (c.getAnnotation(ClientWidget.class) != null) {
                paintables.add((Class<? extends Paintable>) c);
                // System.out.println("Found paintable " + fullclassName);
            } else if (c.getAnnotation(ClientCriterion.class) != null) {
                acceptCriterion.add((Class<? extends AcceptCriterion>) c);
            }

        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
        } catch (LinkageError e) {
            // NOP
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}