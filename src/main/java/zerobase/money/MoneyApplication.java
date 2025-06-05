package zerobase.money;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyApplication.class, args);

		try {
			Connection connect = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&period1=99153000&period2=1749037362");
			Document document = connect.get();

			Elements eles = document.getElementsByAttributeValue("data-testid", "history-table");


			if (!eles.isEmpty()) {
				Element tableElement = eles.get(0);

				Element tbody = tableElement.getElementsByTag("tbody").first();

				if (tbody != null) {
					for (Element e : tbody.children()) {
						String currentText = e.text();


						if (!currentText.endsWith("Dividend")) {
							continue;
						}


						String[] splits = currentText.split(" ");


						if (splits.length >= 4) {
							String month = splits[0];

							int day = Integer.parseInt(splits[1].replace(",", ""));
							int year = Integer.parseInt(splits[2]);
							String dividend = splits[3];

							System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
						} else {

							System.err.println("경고: 예상치 못한 형식의 배당금 행이 발견되었습니다 (파싱 실패): " + currentText);
						}

					}
				} else {
					System.out.println("History table 내에서 tbody 태그를 찾지 못했습니다.");
				}

			} else {
				System.out.println("History table (data-testid='history-table')을 찾지 못했습니다.");
			}


		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("스크래핑 중 오류 발생: " + e.getMessage());
		}


	}
}

