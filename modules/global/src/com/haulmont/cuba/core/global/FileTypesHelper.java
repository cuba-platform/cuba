/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.global;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FileTypesHelper {

    /**
     * Default mime-type.
     */
    public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    private static final String initialExtToMIMEMap = "application/vnd.ms-word.document.macroenabled.12   docm,"
            + "application/vnd.ms-word.template.macroenabled.12                                     dotm,"
            + "application/vnd.openxmlformats-officedocument.wordprocessingml.document              docx,"
            + "application/vnd.openxmlformats-officedocument.wordprocessingml.template              dotx,"
            + "application/vnd.ms-powerpoint.template.macroenabled.12                               potm,"
            + "application/vnd.openxmlformats-officedocument.presentationml.template                potx,"
            + "application/vnd.ms-powerpoint.addin.macroenabled.12                                  ppam,"
            + "application/vnd.ms-powerpoint.slideshow.macroenabled.12                              ppsm,"
            + "application/vnd.openxmlformats-officedocument.presentationml.slideshow               ppsx,"
            + "application/vnd.ms-powerpoint.presentation.macroenabled.12                           pptm,"
            + "application/vnd.openxmlformats-officedocument.presentationml.presentation            pptx,"
            + "application/vnd.ms-excel.addin.macroenabled.12                                       xlam,"
            + "application/vnd.ms-excel.sheet.binary.macroenabled.12                                xlsb,"
            + "application/vnd.ms-excel.sheet.macroenabled.12                                       xlsm,"
            + "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet                    xlsx,"
            + "application/vnd.ms-excel.template.macroenabled.12                                    xltm,"
            + "application/vnd.openxmlformats-officedocument.spreadsheetml.template                 xltx,"
            + "application/vnd.ms-works                        wps wks wcm wdb,"
            + "application/epub+zip                            epub,"
            + "application/cu-seeme                            csm cu,"
            + "application/dsptype                             tsp,"
            + "application/mac-binhex40                        hqx,"
            + "application/msword                              doc dot,"
            + "application/octet-stream                        bin dms lrf mar so dist distz pkg bpk dump elc deploy,"
            + "application/oda                                 oda,"
            + "application/pdf                                 pdf,"
            + "application/pgp-encrypted                       pgp,"
            + "application/pgp-signature                       asc sig,"
            + "application/postscript                          ps ai eps,"
            + "application/rtf                                 rtf,"
            + "application/vnd.ms-excel                        xlb xls xlm xla xlc xlt xlw,"
            + "application/vnd.ms-powerpoint                   ppt pps pot,"
            + "application/vnd.wap.wmlc                        wmlc,"
            + "application/vnd.wap.wmlscriptc                  wmlsc,"
            + "application/wordperfect5.1                      wp5,"
            + "application/zip                                 zip,"
            + "application/x-msaccess                          mdb,"
            + "application/x-futuresplash                      spl,"
            + "application/x-123                               wk,"
            + "application/x-bittorrent                        torrent,"
            + "application/x-bcpio                             bcpio,"
            + "application/x-chess-pgn                         pgn,"
            + "application/x-cpio                              cpio,"
            + "application/x-debian-package                    deb udeb,"
            + "application/x-director                          dir dcr dxr cst cct cxt w3d fgd swa,"
            + "application/x-dms                               dms,"
            + "application/x-dvi                               dvi,"
            + "application/x-xfig                              fig,"
            + "application/x-font-ghostscript                  gsf,"
            + "application/x-font-pcf                          pcf pcf.Z,"
            + "application/x-font-type1                        pfa pfb pfm afm,"
            + "application/x-gnumeric                          gnumeric,"
            + "application/x-gtar                              gtar tgz taz,"
            + "application/x-hdf                               hdf,"
            + "application/x-httpd-php                         phtml pht php,"
            + "application/x-httpd-php3                        php3,"
            + "application/x-httpd-php3-source                 phps,"
            + "application/x-httpd-php3-preprocessed           php3p,"
            + "application/x-httpd-php4                        php4,"
            + "application/x-7z-compressed                     7z"
            + "application/x-ica                               ica,"
            + "application/java-archive                        jar,"
            + "application/java-serialized-object              ser,"
            + "application/java-vm                             class,"
            + "application/javascript                          js,"
            + "application/json                                json,"
            + "application/jsonml+json                         jsonml"
            + "application/vnd.kde.kchart                      chrt,"
            + "application/x-killustrator                      kil,"
            + "application/vnd.kde.kpresenter                  kpr kpt,"
            + "application/vnd.kde.kspread                     ksp,"
            + "application/vnd.kde.kword                       kwd kwt,"
            + "application/x-latex                             latex,"
            + "application/x-lzh-compressed                    lzh lha,"
            + "application/x-lzx                               lzx,"
            + "application/x-maker                             frm fb fbdoc,"
            + "application/vnd.framemaker                      fm frame maker book,"
            + "application/vnd.mif                             mif,"
            + "application/x-msdownload                        exe dll com bat msi,"
            + "application/x-netcdf                            nc cdf,"
            + "application/x-ns-proxy-autoconfig               pac,"
            + "application/x-object                            o,"
            + "application/ogg                                 ogx"
            + "application/x-oz-application                    oza,"
            + "application/x-perl                              pl pm,"
            + "application/pkix-crl                            crl,"
            + "application/x-redhat-package-manager            rpm,"
            + "application/x-shar                              shar,"
            + "application/x-shockwave-flash                   swf swfl,"
            + "application/vnd.stardivision.impress            sdd,"
            + "application/vnd.stardivision.draw               sda,"
            + "application/x-stuffit                           sit,"
            + "application/x-sv4cpio                           sv4cpio,"
            + "application/x-sv4crc                            sv4crc,"
            + "application/x-tar                               tar,"
            + "application/x-tex-gf                            gf,"
            + "application/x-tex-pk                            pk PK,"
            + "application/x-texinfo                           texinfo texi,"
            + "application/x-trash                             ~ % bak old sik,"
            + "application/x-troff                             t tr roff man me ms,"
            + "application/x-ustar                             ustar,"
            + "application/x-wais-source                       src,"
            + "application/x-wingz                             wz,"
            + "application/x-x509-ca-cert                      der crt,"
            + "application/x-rar-compressed                    rar,"
            + "audio/basic                                     au snd,"
            + "audio/midi                                      mid midi kar rmi,"
            + "audio/mpeg                                      mpga mpega mp2 mp2a mp3 m2a m3a,"
            + "audio/x-mpegurl                                 m3u,"
            + "image/x-mrsid-image                             sid,"
            + "audio/x-aiff                                    aif aiff aifc,"
            + "audio/x-gsm                                     gsm,"
            + "audio/x-pn-realaudio                            ra rm ram,"
            + "application/pls+xml                             pls,"
            + "audio/x-wav                                     wav,"
            + "audio/aac                                       aac,"
            + "audio/ogg                                       oga ogg spx,"
            + "chemical/x-csml                                 csml,"
            + "chemical/x-cml                                  cml,"
            + "image/bmp                                       bmp bm,"
            + "image/gif                                       gif,"
            + "image/ief                                       ief,"
            + "image/jpeg                                      jpeg jpg jpe,"
            + "image/x-pcx                                     pcx,"
            + "image/png                                       png,"
            + "image/svg+xml                                   svg,"
            + "image/tiff                                      tiff tif,"
            + "image/vnd.wap.wbmp                              wbmp,"
            + "image/x-cmu-raster                              ras,"
            + "image/x-coreldraw                               cdr,"
            + "image/x-coreldrawpattern                        pat,"
            + "image/x-coreldrawtemplate                       cdt,"
            + "image/x-corelphotopaint                         cpt,"
            + "application/mac-compactpro                      cpt,"
            + "image/x-jng                                     jng,"
            + "image/x-portable-anymap                         pnm,"
            + "image/x-portable-bitmap                         pbm,"
            + "image/x-portable-graymap                        pgm,"
            + "image/x-portable-pixmap                         ppm,"
            + "image/x-rgb                                     rgb,"
            + "image/x-xbitmap                                 xbm,"
            + "image/x-xpixmap                                 xpm,"
            + "image/x-xwindowdump                             xwd,"
            + "text/csv                                        csv,"
            + "text/css                                        css,"
            + "text/html                                       htm html,"
            + "application/xhtml+xml                           xhtml xht,"
            + "text/mathml                                     mml,"
            + "text/plain                                      txt text diff conf def list log in,"
            + "text/richtext                                   rtx,"
            + "text/tab-separated-values                       tsv,"
            + "text/vnd.wap.wml                                wml,"
            + "text/vnd.wap.wmlscript                          wmls,"
            + "text/xml                                        xml,"
            + "text/x-c++hdr                                   h++ hpp hxx,"
            + "text/x-c++src                                   c++,"
            + "text/x-c                                        c cc cxx cpp h hh dic,"
            + "text/x-csh                                      csh,"
            + "text/x-java-source                              java,"
            + "text/x-moc                                      moc,"
            + "text/x-pascal                                   p pas,"
            + "text/x-setext                                   etx,"
            + "text/x-sh                                       sh,"
            + "text/x-tcl                                      tcl tk,"
            + "text/x-tex                                      tex ltx sty cls,"
            + "text/x-vcalendar                                vcs,"
            + "text/x-vcard                                    vcf,"
            + "video/dl                                        dl,"
            + "video/x-fli                                     fli,"
            + "video/gl                                        gl,"
            + "video/mpeg                                      mpeg mpg mpe m1v m2v,"
            + "video/quicktime                                 qt mov,"
            + "video/x-mng                                     mng,"
            + "video/x-ms-asf                                  asf asx,"
            + "video/x-msvideo                                 avi,"
            + "video/x-sgi-movie                               movie,"
            + "x-world/x-vrml                                  vrm,"
            + "model/vrml                                      wrl vrml,"
            + "application/x-font-otf                          otf,"
            + "application/vnd.oasis.opendocument.text                      odt,"
            + "application/vnd.oasis.opendocument.text-template             ott,"
            + "application/vnd.oasis.opendocument.graphics                  odg,"
            + "application/vnd.oasis.opendocument.graphics-template         otg,"
            + "application/vnd.oasis.opendocument.presentation              odp,"
            + "application/vnd.oasis.opendocument.presentation-template     otp,"
            + "application/vnd.oasis.opendocument.spreadsheet               ods,"
            + "application/vnd.oasis.opendocument.spreadsheet-template      ots,"
            + "application/vnd.oasis.opendocument.chart                     odc,"
            + "application/vnd.oasis.opendocument.chart-template            otc,"
            + "application/vnd.oasis.opendocument.image                     odi,"
            + "application/vnd.oasis.opendocument.image-template            oti,"
            + "application/vnd.oasis.opendocument.formula                   odf,"
            + "application/vnd.oasis.opendocument.formula-template          odft,"
            + "application/vnd.oasis.opendocument.text-master               odm,"
            + "application/vnd.oasis.opendocument.text-web                  oth";

    /**
     * File extension to MIME type mapping.
     */
    private static Map<String, String> extToMIMEMap = new HashMap<>();

    static {
        // Initialize extension to MIME map
        final StringTokenizer lines = new StringTokenizer(initialExtToMIMEMap,
                ",");
        while (lines.hasMoreTokens()) {
            final String line = lines.nextToken();
            final StringTokenizer exts = new StringTokenizer(line);
            final String type = exts.nextToken();
            while (exts.hasMoreTokens()) {
                final String ext = exts.nextToken();
                addExtension(ext, type);
            }
        }
    }

    /**
     * Gets the mime-type of a file. Currently the mime-type is resolved based
     * only on the file name extension.
     *
     * @param fileName
     *            the name of the file whose mime-type is requested.
     * @return mime-type <code>String</code> for the given filename
     */
    public static String getMIMEType(String fileName) {

        // Checks for nulls
        if (fileName == null) {
            throw new NullPointerException("Filename can not be null");
        }

        // Calculates the extension of the file
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1) {
            final String ext = StringUtils.substring(fileName, dotIndex + 1).toLowerCase();

            // Return type from extension map, if found
            final String type = extToMIMEMap.get(ext);
            if (type != null) {
                return type;
            }
        }

        return DEFAULT_MIME_TYPE;
    }

    /**
     * Gets the mime-type for a file. Currently the returned file type is
     * resolved by the filename extension only.
     *
     * @param file
     *            the file whose mime-type is requested.
     * @return the files mime-type <code>String</code>
     */
    public static String getMIMEType(File file) {

        // Checks for nulls
        if (file == null) {
            throw new NullPointerException("File can not be null");
        }

        // Directories
        if (file.isDirectory()) {
            // Drives
            if (file.getParentFile() == null) {
                return "inode/drive";
            } else {
                return "inode/directory";
            }
        }

        // Return type from extension
        return getMIMEType(file.getName());
    }

    /**
     * Adds a mime-type mapping for the given filename extension. If the
     * extension is already in the internal mapping it is overwritten.
     *
     * @param extension
     *            the filename extension to be associated with
     *            <code>MIMEType</code>.
     * @param MIMEType
     *            the new mime-type for <code>extension</code>.
     */
    public static void addExtension(String extension, String MIMEType) {
        extToMIMEMap.put(extension, MIMEType);
    }

    /**
     * Gets the internal file extension to mime-type mapping.
     *
     * @return unmodifiable map containing the current file extension to
     *         mime-type mapping
     */
    public static Map getExtensionToMIMETypeMapping() {
        return Collections.unmodifiableMap(extToMIMEMap);
    }
}