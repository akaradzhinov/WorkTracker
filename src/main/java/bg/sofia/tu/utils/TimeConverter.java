package bg.sofia.tu.utils;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 18/09/2016 @ 15:18.
 */
public class TimeConverter {

    //time format:  2w 5d 20h

    public static int convertStringToHours(String timeString) {
        int result = 0;

        for(String timeFrame : timeString.trim().split(" ")) {
            int digit = Integer.parseInt(timeFrame.replaceAll("\\D+",""));
            String time = timeFrame.replaceAll("[^a-z]", "");

            switch (time) {
                case "w":
                    result += digit * 168;
                    break;
                case "d":
                    result += digit * 24;
                    break;

                case "h":
                    result += digit;
                    break;

                default:
                    throw new IllegalArgumentException("Not supported time variable!");
            }
        }

        return result;
    }

    public static String convertTimeToString(int weeks, int days, int hours) {
        return convertHoursToString((weeks * 168) + (days * 24) + hours);
    }

    public static String convertHoursToString(int time) {
        StringBuilder builder = new StringBuilder();

        int weeks = time / 168;
        builder.append(weeks + "w ");

        int days = (time % 168) / 24;
        builder.append(days + "d ");

        int hours = (time % 168) % 24;
        builder.append(hours + "h");

        return builder.toString().trim();
    }
}
