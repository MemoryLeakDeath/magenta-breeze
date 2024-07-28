package tv.memoryleakdeath.magentabreeze.app;

import java.io.IOException;
import java.io.InputStream;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecureStorageUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecureStorageUtil.class);
    private static final String STORAGE_FILE = "keystore.db";
    private static final String KEY_FILE = "keystore.mstore";
    private static final int CODE_POINT = 0x301;
    private static final String KEYSTORE_NAME = "keys";

    private SecureStorageUtil() {
    }

    private static String getCurrentKey() {
        try (InputStream is = SecureStorageUtil.class.getClassLoader().getResourceAsStream(KEY_FILE)) {
            String encryptedKey = new String(is.readAllBytes());
            return encryptedKey.chars().map(c -> c ^ CODE_POINT)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        } catch (Exception e) {
            logger.error("Failed to read key file!", e);
        }
        return null;
    }

    private static String getResourceFilePath() throws IOException {
        return SecureStorageUtil.class.getClassLoader().getResource(STORAGE_FILE).getPath();
    }

    private static MVStore getMVStore(String resourcePath, String currentKey) {
        return new MVStore.Builder().fileName(resourcePath).encryptionKey(currentKey.toCharArray()).compress().open();
    }

    public static String getKeyValueFromSecureStorage(String key) {
        String value = null;
        try {
            MVStore store = getMVStore(getResourceFilePath(), getCurrentKey());
            MVMap<String, String> storedValues = store.openMap(KEYSTORE_NAME);
            value = storedValues.get(key);
            store.close();
        } catch (Exception e) {
            logger.error("Failed to read value from secure storage!", e);
        }
        return value;
    }
}
