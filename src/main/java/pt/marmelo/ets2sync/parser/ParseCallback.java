package pt.marmelo.ets2sync.parser;

public interface ParseCallback {
    void apply(Context context, String name, String value, String sourceValue, long offset);
}
