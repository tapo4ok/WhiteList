package mdk.mutils.api.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleConfig<T> {
    private String fileName;
    private String packageName;
    private T config;
    @Deprecated
    public final Config.Type type;
    private final Class<T> cls;
    private File configFile;

    public SimpleConfig(Class<T> cls, Config.Type type, Logger logger, File file) {
        this.type = type;
        this.cls = cls;
        Config fs = (Config)cls.getAnnotation(Config.class);
        if (fs != null) {
            String name;
            if (fs.value().equalsIgnoreCase("")) {
                name = cls.getSimpleName().toLowerCase();
            } else {
                name = fs.value();
            }

            name = name.toLowerCase();
            this.fileName = name;
            Config.Package f2 = (Config.Package)cls.getAnnotation(Config.Package.class);
            if (f2 == null) {
                this.packageName = null;
                this.func_0c(logger, file);
            } else {
                String packag;
                if (f2.value().equalsIgnoreCase("")) {
                    packag = cls.getPackage().getName();
                } else {
                    packag = fs.value();
                }

                packag = packag.toLowerCase().replace('.', '\\').replace('\\', '/');
                this.packageName = packag;
                this.func_0c(logger, file);
            }
        }
    }

    private void func_0c(Logger logger, File dataFolder) {
        dataFolder = dataFolder.getParentFile();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File configDir = new File(dataFolder, "configs");
        File pkgDir;
        if (this.packageName != null) {
            pkgDir = new File(configDir, this.packageName);
        } else {
            pkgDir = new File(configDir.toURI());
        }

        if (!pkgDir.exists()) {
            pkgDir.mkdirs();
        }

        this.configFile = new File(pkgDir, this.fileName + this.type.pa);
        if (!this.configFile.exists()) {
            try {
                Config fs = (Config)this.cls.getAnnotation(Config.class);
                String f = fs.FromResource();
                if (f.equalsIgnoreCase("false")) {
                    this.Save();
                } else {
                    this.saveResource(f, logger);
                }
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        try {
            this.Load();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public void Load() throws IOException {
        this.config = Static.GSON.fromJson(new FileReader(this.configFile), this.cls);

    }

    public void Save() throws IOException, InstantiationException, IllegalAccessException {
        if (this.config == null) {
            this.config = this.cls.newInstance();
        }

        FileWriter fileWriter = new FileWriter(this.configFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(Static.GSON.toJson(this.config));
        bufferedWriter.close();
    }

    public T getConfig() {
        return this.config;
    }

    public void saveResource(String resourcePath, Logger logger) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResource(resourcePath, this.cls);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + this.configFile);
            } else {
                File outDir = this.configFile.getParentFile();
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (!this.configFile.exists()) {
                        OutputStream out = new FileOutputStream(this.configFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    } else {
                        logger.log(Level.WARNING, "Could not save " + this.configFile.getName() + " to " + this.configFile + " because " + this.configFile.getName() + " already exists.");
                    }
                } catch (IOException var9) {
                    logger.log(Level.SEVERE, "Could not save " + this.configFile.getName() + " to " + this.configFile, var9);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    public InputStream getResource(String filename, Class<T> cls) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else {
            try {
                URL url = cls.getClassLoader().getResource(filename);
                if (url == null) {
                    return null;
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    return connection.getInputStream();
                }
            } catch (IOException var5) {
                return null;
            }
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public Config.Type getType() {
        return this.type;
    }

    public Class<T> getCls() {
        return this.cls;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public String toString() {
        return "SimpleConfig(fileName=" + this.getFileName() + ", packageName=" + this.getPackageName() + ", config=" + this.getConfig() + ", type=" + this.getType() + ", cls=" + this.getCls() + ", configFile=" + this.getConfigFile() + ")";
    }
}
