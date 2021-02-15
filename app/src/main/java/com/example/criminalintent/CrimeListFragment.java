package com.example.criminalintent;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;

    }

    private void updateUI() {
        mAdapter = new CrimeAdapter(CrimeLab.get(getActivity()).getCrimes());
        mCrimeRecyclerView.setAdapter(mAdapter);
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            Date date = mCrime.getDate();
            String format = DateFormat.getMediumDateFormat(getActivity()).format(date);
            mDateTextView.setText(format);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class NormalCrimeHolder extends CrimeHolder {
        private final ImageView mSolvedImageView;

        public NormalCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            mSolvedImageView = itemView.findViewById(R.id.crime_solved);

        }

        @Override
        public void bind(Crime crime) {
            super.bind(crime);
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

        }
    }

    private class SeriousCrimeHolder extends CrimeHolder {

        private final Button mCrimeContactPolice;

        public SeriousCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_serious, parent, false));

            mCrimeContactPolice = itemView.findViewById(R.id.crime_contact_police);
            mCrimeContactPolice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "police contacted!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private final List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            if (viewType == 0) {
                return new NormalCrimeHolder(inflater, parent);

            } else {
                return new SeriousCrimeHolder(inflater, parent);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mCrimes.get(position).isRequiresPolice() ? 1 : 0;
        }
    }
}
