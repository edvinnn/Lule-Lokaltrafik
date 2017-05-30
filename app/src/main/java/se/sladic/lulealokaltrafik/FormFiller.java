package se.sladic.lulealokaltrafik;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FormFiller extends AsyncTask {

    org.jsoup.nodes.Document doc;
    String urlQuery     = "http://193.45.213.123/lulea/v2/querypage_adv.aspx";
    String urlResult    = "http://193.45.213.123/lulea/v2/resultspage.aspx";

    private OnTaskCompleted taskCompleted;

    public FormFiller(OnTaskCompleted taskCompleted){
        this.taskCompleted = taskCompleted;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Object doInBackground(Object[] params) {
            try {
                doc = Jsoup.connect(urlQuery)
                        .data("inpPointFr", "Smedjegatan"/*params[0].toString()*/)
                        .data("inpPointTo", "Porsön"/*params[1].toString()*/)
                        .data("inpTime", "12:00"/*params[2].toString()*/)
                        .data("inpDate", "2017-05-31"/*params[3].toString()*/)
                        .data("inpTime2", "14:00"/*params[4].toString()*/)
                        .data("inpDate2", "2017-05-31"/*params[5].toString()*/)
                        .data("cmdAction", "search")
                        .data("VerNo", "7.1.1.5.170.5685")
                        .data("Source", "querypage_adv")
                        .post();

                doc = Jsoup.connect(urlResult)
                        .data("selPointFr", doc.select("select[name=selPointFr]").select("select > option").first().val())
                        .data("selPointTo", doc.select("select[name=selPointTo]").select("select > option").first().val())
                        .data("inpTime", "12:00"/*params[2].toString()*/)
                        .data("inpDate", "2017-05-31"/*params[3].toString()*/)
                        .data("inpTime2", "14:00"/*params[4].toString()*/)
                        .data("inpDate2", "2017-05-31"/*params[5].toString()*/)
                        .data("cmdAction", "search")
                        .data("VerNo", "7.1.1.5.170.5685")
                        .data("Source", "querypage_adv")
                        .post();

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<Result> resultsArray = new ArrayList<>();
            Elements results = doc.select("tr#result-0");
            for(int i = 0; i < 4; i++) {

                Result bus = new Result();
                bus.departureTime   = results.select("tr > td:nth-of-type(2)").text();
                bus.arrivalTime     = results.select("tr > td:nth-of-type(3)").text();
                bus.travelTime      = results.select("tr > td:nth-of-type(4)").text();
                bus.hops            = results.select("tr > td:nth-of-type(5)").text();

                results = results.next();

                bus.line            = results.select("tr > td:nth-of-type(1) > div:nth-of-type(1) > table > tbody > tr:nth-of-type(2) > td:nth-of-type(2) > a").first().text();
                bus.print();

                resultsArray.add(bus);
                results = results.next();
            }

        return resultsArray;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        taskCompleted.onTaskCompleted(o);
        //super.onPostExecute(o);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(Object response);
    }
}
