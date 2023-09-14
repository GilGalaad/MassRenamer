package massrenamer.common;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Log4j2
public class CommonUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static String getFileExtension(String filename) {
        int dotPosition = filename.lastIndexOf(".");
        if (dotPosition == -1) {
            return "";
        }
        return filename.substring(dotPosition + 1).toLowerCase().trim();
    }

    public static int calcPercent(long finishedItems, long totalItems) {
        if (totalItems == 0) {
            return 0;
        }
        return BigDecimal.valueOf(finishedItems).multiply(BigDecimal.valueOf(100L)).divide(BigDecimal.valueOf(totalItems), 0, RoundingMode.HALF_UP).toBigInteger().intValue();
    }

    public static String smartElapsed(long elapsedNano) {
        return smartElapsed(elapsedNano, 1);
    }

    public static String smartElapsed(long elapsedNano, int scale) {
        if (elapsedNano < TimeUnit.MICROSECONDS.toNanos(1)) {
            return elapsedNano + " nsec";
        } else if (elapsedNano < TimeUnit.MILLISECONDS.toNanos(1)) {
            return BigDecimal.valueOf(elapsedNano).divide(BigDecimal.valueOf(TimeUnit.MICROSECONDS.toNanos(1)), scale, RoundingMode.HALF_UP) + " usec";
        } else if (elapsedNano < TimeUnit.SECONDS.toNanos(1)) {
            return BigDecimal.valueOf(elapsedNano).divide(BigDecimal.valueOf(TimeUnit.MILLISECONDS.toNanos(1)), scale, RoundingMode.HALF_UP) + " msec";
        } else if (elapsedNano < TimeUnit.MINUTES.toNanos(1)) {
            return BigDecimal.valueOf(elapsedNano).divide(BigDecimal.valueOf(TimeUnit.SECONDS.toNanos(1)), scale, RoundingMode.HALF_UP) + " sec";
        } else if (elapsedNano < TimeUnit.HOURS.toNanos(1)) {
            return BigDecimal.valueOf(elapsedNano).divide(BigDecimal.valueOf(TimeUnit.MINUTES.toNanos(1)), scale, RoundingMode.HALF_UP) + " min";
        } else {
            return BigDecimal.valueOf(elapsedNano).divide(BigDecimal.valueOf(TimeUnit.HOURS.toNanos(1)), scale, RoundingMode.HALF_UP) + " hours";
        }
    }

}
