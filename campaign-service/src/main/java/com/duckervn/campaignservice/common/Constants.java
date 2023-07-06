package com.duckervn.campaignservice.common;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String REQUEST =  "-----------------------------------REQUEST-----------------------------------";

    public static final String RESPONSE = "-----------------------------------RESPONSE----------------------------------";
    public static final String ACTIVE = "ACTIVE";

    public static final String INACTIVE = "INACTIVE";

    public static final List<String> STATUSES = Arrays.asList(ACTIVE, INACTIVE);

    public static final String WAITING = "WAITING";

    public static final String SUCCEEDED = "SUCCEEDED";

    public static final String FAILED = "FAILED";

    public static final String CANCELED = "CANCELED";

    public static final List<String> CAMPAIGN_RECIPIENT_STATUSES = Arrays.asList(WAITING, SUCCEEDED, FAILED, CANCELED);

    public static final String EMAIL = "EMAIL";

    public static final String SMS = "SMS";

    public static final List<String> MESSENGER_TYPES = Arrays.asList(EMAIL, SMS);

    public static final String SMTP = "SMTP";

    public static final String API = "API";

    public static final List<String> SEND_METHODS = Arrays.asList(SMTP, API);

    public static final Integer MAX_RETRY = 3;
}
