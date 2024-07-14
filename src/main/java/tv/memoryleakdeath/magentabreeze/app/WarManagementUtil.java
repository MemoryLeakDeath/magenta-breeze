package tv.memoryleakdeath.magentabreeze.app;

import java.io.File;

public final class WarManagementUtil {
    private static final String WAR_FILENAME = "magenta-breeze-0.0.1-SNAPSHOT.war";

    private WarManagementUtil() {
    }

    public static WarStatus isWarOk(String installDirectory) {
        File warFile = new File(installDirectory + File.separator + WAR_FILENAME);
        if (!warFile.exists()) {
            return WarStatus.MISSING;
        }
        return WarStatus.OK;
    }
}
