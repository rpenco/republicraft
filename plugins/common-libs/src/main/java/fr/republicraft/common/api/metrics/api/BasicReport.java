package fr.republicraft.common.api.metrics.api;

import com.google.gson.annotations.SerializedName;
import fr.republicraft.common.api.helper.JsonHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Default reporter
 */
public class BasicReport extends AbstractReport {

    @SerializedName("@timestamp")
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

    /**
     * Obtains JSON String for this reporter
     *
     * @return
     */
    @Override
    public String toString() {
        return JsonHelper.toJson(this);
    }
}
