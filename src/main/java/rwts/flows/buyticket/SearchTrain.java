package rwts.flows.buyticket;

import org.openqa.selenium.WebDriver;
import rwts.beans.Order;
import rwts.flows.FlowUnitExecutionException;
import rwts.pages.RouteChoosePage;
import rwts.pages.TrainSelectPage;

/**
 * @author Serj Sintsov
 * @since 11/5/13 8:58 PM
 */
public class SearchTrain extends BaseFlowUnit
{
    private final static int MAX_ATTEMPTS_COUNT = 1000;

    private TrainSelectPage trainSelectPage;
    private RouteChoosePage routeChoosePage;
    private Order order;

    public SearchTrain(Order order, WebDriver driver)
    {
        routeChoosePage = new RouteChoosePage(driver);
        trainSelectPage = new TrainSelectPage(driver);
        this.order = order;
    }

    @Override
    public void doExecute() throws FlowUnitExecutionException
    {
        int attempt = 0;

        while (attempt < MAX_ATTEMPTS_COUNT)
        {
            tryToChooseRoute();

            if (trainSelectPage.selectTrain(order))
            {
                trainSelectPage.selectGoodTrain();
                trainSelectPage.clickNext();
            }
            else
            {
                trainSelectPage.backToRouteChoosePage();
                System.out.println("No ticket");
            }
        }

        if (attempt == MAX_ATTEMPTS_COUNT)
            throw new FlowUnitExecutionException("Cannot find appropriate train ot tickets");
    }

    private int tryToChooseRoute() {
        int attempt = 0;

        while (attempt < MAX_ATTEMPTS_COUNT && !routeChoosePage.searchPossibleRoute(order))
        {
            attempt++;
            sleep(1000);
        }

        return attempt;
    }

    private void sleep(long ms) {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isMandatory() { return true; }
}
