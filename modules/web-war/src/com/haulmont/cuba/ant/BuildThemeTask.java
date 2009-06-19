package com.haulmont.cuba.ant;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * User: Nikolay Gorodnov
 * Date: 19.06.2009
 */
public class BuildThemeTask extends Task {

    private String themeDir;
    private String destFile;

    @Override
    public void execute() throws BuildException {
        super.execute();
        buildTheme();
    }

    private void buildTheme() {
        try
        {
            File f = new File(themeDir);
            if (!f.isDirectory()) {
                throw new IllegalArgumentException("ThemeDir should be a directory");
            }

            String themeName = f.getName();

            StringBuffer combinedCss = new StringBuffer();
            combinedCss
                .append("/* Automatically created css file from subdirectories. */\n");

            final File[] subdir = f.listFiles();
            Arrays.sort(subdir, new Comparator<File>() {
                public int compare(File arg0, File arg1) {
                    return (arg0).compareTo(arg1);
                }
            });

            for (final File dir : subdir) {
                String name = dir.getName();
                String filename = dir.getPath() + "/" + name + ".css";

                final File cssFile = new File(filename);
                if (cssFile.isFile()) {

                    combinedCss.append("\n");
                    combinedCss.append("/* >>>>> ").append(cssFile.getName()).append(" <<<<< */");
                    combinedCss.append("\n");

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(new FileInputStream(cssFile)));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        String urlPrefix = "../" + themeName + "/";

                        if (strLine.indexOf("url(../") > 0) {
                            strLine = strLine.replaceAll("url\\(../",
                                    ("url\\(" + urlPrefix));

                        } else {
                            strLine = strLine.replaceAll("url\\(", ("url\\("
                                    + urlPrefix + name + "/"));

                        }
                        combinedCss.append(strLine);
                        combinedCss.append("\n");
                    }
                    br.close();
                }
            }

            if (!themeDir.endsWith("/")) {
                themeDir += "/";
            }

            if (destFile.indexOf(".") == -1) {
                destFile += ".css";
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(themeDir + destFile));
            out.write(combinedCss.toString());
            out.close();

            System.out.println("Compiled CSS to " + themeDir + destFile
                    + " (" + combinedCss.toString().length() + " bytes)");
        }
        catch (IOException e) {
            throw new BuildException(e);
        }
    }

    public String getThemeDir() {
        return themeDir;
    }

    public void setThemeDir(String themeDir) {
        this.themeDir = themeDir;
    }

    public String getDestFile() {
        return destFile;
    }

    public void setDestFile(String destFile) {
        this.destFile = destFile;
    }
}
