package com.marbit.hobbytrophies.interfaces;

import com.marbit.hobbytrophies.model.User;

import java.util.List;

public interface RankingPresenterInterface {
    void onSuccessLoadRanking(List<User> lastPodium, List<User> listRanking);
}
