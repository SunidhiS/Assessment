package coding_assignment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;



public class BitcoinRateInWords {
	public static void main(String[] args) {
        try {
            // Create URL object for the Bitcoin API
            URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code from the API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                // Parse the JSON response
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Extract the necessary information from the JSON response
                JSONObject bpi = null;
                try {
                    bpi = jsonResponse.getJSONObject("bpi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject usd = null;
                try {
                    usd = bpi.getJSONObject("USD");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String rate = null;
                try {
                    rate = usd.getString("rate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Convert the rate to words
                String rateInWords = convertToWords(rate);
                System.out.println(rateInWords);
            } else {
                System.out.println("Error: HTTP response code - " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertToWords(String rate) {
        // Arrays holding the words for digits, tens, and thousands
        String[] digits = {
                "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
        };

        String[] tens = {
                "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
        };

        String[] thousands = {
                "", "Thousand", "Million", "Billion", "Trillion"
        };

        // Splitting the rate into integer and decimal parts
        String[] parts = rate.split("\\.");
        String integerPart = parts[0].replace(",", "");
        int len = integerPart.length();
        StringBuilder result = new StringBuilder();

        // Handling the special case where the rate is zero
        if (len == 1 && integerPart.charAt(0) == '0') {
            result.append("Zero");
        } else {
            int group = 0;
            int count;

            while (len > 0) {
                count = Math.min(3, len);
                String groupStr = integerPart.substring(len - count, len);

                int groupValue = Integer.parseInt(groupStr);
                if (groupValue != 0) {
                    StringBuilder groupResult = new StringBuilder();

                    // Handling the hundreds place
                    if (groupValue >= 100) {
                        int hundredDigit = groupValue / 100;
                        groupResult.append(digits[hundredDigit]).append(" Hundred ");
                        groupValue %= 100;
                    }

                    // Handling the tens and ones place
                    if (groupValue >= 20) {
                        int tensDigit = groupValue / 10;
                        groupResult.append(tens[tensDigit]).append(" ");
                        groupValue %= 10;
                    }

                    if (groupValue > 0) {
                        groupResult.append(digits[groupValue]).append(" ");
                    }

                    // Appending the thousands unit
                    groupResult.append(thousands[group]);

                    // Prepending the group result to the final result
                    result.insert(0, groupResult).insert(0, " ");
                }

                len -= count;
                group++;
            }
        }

        return result.toString().trim();
    }

}
