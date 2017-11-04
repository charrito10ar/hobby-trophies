package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.dialogs.adapter.SearchGameAdapter;
import com.marbit.hobbytrophies.model.Game;

import java.util.ArrayList;
import java.util.List;

public class DialogSearchLocalGame extends DialogFragment implements TextWatcher {
    private static final String ARG_PARAM1 = "user-games";
    private DialogSearchLocalGameListener mListener;
    private EditText editTextSearchGame;
    private List<Game> userGames;
    private RecyclerView recyclerView;
    private SearchGameAdapter adapter;

    public static DialogSearchLocalGame newInstance(ArrayList<Game> userGames) {
        DialogSearchLocalGame dialogSearchGame = new DialogSearchLocalGame();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, userGames);
        dialogSearchGame.setArguments(args);
        return dialogSearchGame;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userGames = new ArrayList<>();
        if (getArguments() != null) {
            userGames = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search_local_game, null);
        this.editTextSearchGame = (EditText) view.findViewById(R.id.edit_text_search_game);
        this.editTextSearchGame.addTextChangedListener(this);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_games);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.adapter = new SearchGameAdapter(getContext(), userGames, new SearchGameAdapter.SearchGameAdapterListener() {
            @Override
            public void selectGame(Game game) {
                mListener.selectGame(game);
                dismiss();
            }
        });

        this.recyclerView.setAdapter(adapter);
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void refreshContent(List<Game> gameList) {
        this.adapter.clearAll();
        this.adapter.setList(gameList);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adapter.notifyDataSetChanged();
        // refreshContent(userGames);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DialogSearchLocalGameListener){
            mListener = (DialogSearchLocalGameListener) context;
        }else {
            if (getTargetFragment() instanceof DialogSearchLocalGameListener) {
                mListener = (DialogSearchLocalGameListener) getTargetFragment();
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement DialogSearchLocalGameListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public interface DialogSearchLocalGameListener{
        void selectGame(Game game);
    }
}
