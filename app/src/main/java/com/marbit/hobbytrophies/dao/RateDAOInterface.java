package com.marbit.hobbytrophies.dao;


import com.marbit.hobbytrophies.market.model.Rate;

import org.jetbrains.annotations.Nullable;

public interface RateDAOInterface {
    void insertRate(Rate rate, RateDAO.RateListener rateListener);

    void loadRatesByUser(@Nullable String userId, RateDAO.LoadRateListener loadRateListener);
}
