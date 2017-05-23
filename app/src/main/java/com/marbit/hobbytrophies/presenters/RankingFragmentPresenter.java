package com.marbit.hobbytrophies.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.RankingFragmentInteractor;
import com.marbit.hobbytrophies.interfaces.RankingFragmentView;
import com.marbit.hobbytrophies.interfaces.RankingPresenterInterface;
import com.marbit.hobbytrophies.model.User;

import java.util.List;

public class RankingFragmentPresenter implements RankingPresenterInterface{

    private Context context;
    private RankingFragmentInteractor interactor;
    private RankingFragmentView rankingFragmentView;

    public RankingFragmentPresenter(Context context, RankingFragmentView rankingFragmentView) {
        this.context = context;
        this.interactor = new RankingFragmentInteractor(context, this);
        this.rankingFragmentView = rankingFragmentView;
    }

    public void loadRemoteRanking() {
        this.interactor.loadRemoteRanking();
    }

    @Override
    public void onSuccessLoadRanking(List<User> lastPodium, List<User> listRanking) {
        this.rankingFragmentView.setLastWinner(lastPodium);
        this.rankingFragmentView.setRanking(listRanking);
    }

    public void loadLocalRanking() {
        this.interactor.loadLocalRanking();
    }
}
