package com.github.ukasz09;
public interface DataFetchingObserver {

    void onDataFetchingError(String errorMsg);

    void onDataFetched();
}
