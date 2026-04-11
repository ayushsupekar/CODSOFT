import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    static final String API_KEY = "YOUR_API_KEY";
    static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Currency Converter =====");
        System.out.print("Enter base currency (e.g. USD, INR, EUR): ");
        String baseCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter target currency (e.g. USD, INR, EUR): ");
        String targetCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            double rate = fetchExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = amount * rate;

            System.out.printf("%n%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
            System.out.printf("Exchange Rate: 1 %s = %.4f %s%n", baseCurrency, rate, targetCurrency);
        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
        }

        scanner.close();
    }

    static double fetchExchangeRate(String base, String target) throws Exception {
        String urlStr = API_URL + base + "/" + target;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP error code: " + responseCode + ". Check currency codes.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String json = response.toString();
        if (json.contains("\"result\":\"error\"")) {
            throw new Exception("Invalid currency code or API error.");
        }

        String key = "\"conversion_rate\":";
        int idx = json.indexOf(key);
        if (idx == -1) throw new Exception("Could not parse exchange rate.");

        int start = idx + key.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);

        return Double.parseDouble(json.substring(start, end).trim());
    }
}
