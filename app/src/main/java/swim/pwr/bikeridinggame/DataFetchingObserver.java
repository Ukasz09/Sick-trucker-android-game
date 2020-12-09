package swim.pwr.bikeridinggame;

public interface DataFetchingObserver {

    void onDataFetchingError(String errorMsg);

    void onDataFetched();
}
