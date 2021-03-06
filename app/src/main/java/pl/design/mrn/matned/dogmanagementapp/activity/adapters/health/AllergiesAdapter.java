package pl.design.mrn.matned.dogmanagementapp.activity.adapters.health;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pl.design.mrn.matned.dogmanagementapp.R;
import pl.design.mrn.matned.dogmanagementapp.activity.health.AllergiesActivity;
import pl.design.mrn.matned.dogmanagementapp.dataBase.health.Allergies;
import pl.design.mrn.matned.dogmanagementapp.listeners.DataPositionListener;

import static pl.design.mrn.matned.dogmanagementapp.Statics.DATE_FORMAT;


public class AllergiesAdapter extends RecyclerView.Adapter<AllergiesAdapter.ViewHolder> {

    private List<Allergies> allergiesList;
    private DataPositionListener dataPositionListener;
    private int selectedPosition;
    private Context context;

    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public AllergiesAdapter(List<Allergies> allergiesList, Context context) {
        this.context = context;
        this.allergiesList = allergiesList;
        this.dataPositionListener = DataPositionListener.getInstance();
        this.selectedPosition = dataPositionListener.getPosition();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_allergy, null);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Allergies allergies = allergiesList.get(position);
        holder.allergyName.setText(allergies.getAllergen());
        holder.holderButton.setOnClickListener(v -> {
            selectedPosition = position;
            dataPositionListener.setPosition(position);
            dataPositionListener.setSelectedItemId(allergies.getId());
            notifyDataSetChanged();
            Intent intent = new Intent(context, AllergiesActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (allergiesList == null) return 0;
        else return allergiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout holderButton;
        private TextView allergyName;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.holderButton = itemView.findViewById(R.id.allergy_item_button);
            this.allergyName = itemView.findViewById(R.id.allergy_name);
        }


    }

}

