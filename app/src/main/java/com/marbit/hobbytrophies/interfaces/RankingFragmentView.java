package com.marbit.hobbytrophies.interfaces;

import com.marbit.hobbytrophies.model.User;

import java.util.List;

public interface RankingFragmentView {
    void setLastWinner(List<User> winner);

    void setRanking(List<User> listRanking);
}
