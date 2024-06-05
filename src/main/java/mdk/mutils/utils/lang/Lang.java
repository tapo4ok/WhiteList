package mdk.mutils.utils.lang;

import java.io.*;
import java.util.HashMap;

public class Lang extends HashMap<String, String> implements ILang {
    public Lang() {
        super();
    }

    @Override
    public String format(String str, Object... obs) {
        return String.format(get(str), obs);
    }

    @Override
    public ILang load(Reader reader2) {
        try (BufferedReader reader = new BufferedReader(reader2)) {
            boolean end = true;
            boolean commit = false;
            String line;
            String prefix = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<")) {
                    if (!line.contains(">")) {
                        commit = true;
                    }
                } else
                if (commit) {
                    if (line.contains(">")) {
                        commit = false;
                    }
                } else
                if (line.startsWith("#{") && line.endsWith("}")) {
                    prefix = line.substring(2, line.length() - 1);
                    end = false;
                } else if (line.startsWith("#end")) {
                    end = true;
                } else if (line.startsWith("//")) {

                } else if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    String key;
                    if (!end) {
                        key = prefix + parts[0].trim();
                    }
                    else {
                        key = parts[0].trim();
                    }
                    put(key, parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public ILang load(String code) {
        return load(new StringReader(code));
    }

    @Override
    public ILang load(File file) {
        try {
            return load(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    @Override
    public ILang load(DataInputStream stream) {
        return load(new InputStreamReader(stream));
    }
    @Override
    public ILang load(InputStream stream) {
        return load(new InputStreamReader(stream));
    }
}
