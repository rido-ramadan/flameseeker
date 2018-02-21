package com.edgardrake.flameseeker.activity.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edgardrake.flameseeker.R;
import com.edgardrake.flameseeker.lib.base.BaseFragment;
import com.edgardrake.flameseeker.lib.widget.carousel.CarouselView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselFragment extends BaseFragment {

    @BindView(R.id.carousel_view)
    CarouselView mCarousel;

    private List<CarouselData> dataset;
    private List<String> imageSrcs;

    public CarouselFragment() {}

    public static CarouselFragment newInstance() {
        CarouselFragment fragment = new CarouselFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CarouselData[] imgs = new CarouselData[] {
            new CarouselData("https://www.fightersgeneration.com/nf/char2/noctis/1/2/noctis-ffxv-early-portrait.jpg", "Noctis"),
            new CarouselData("https://vignette.wikia.nocookie.net/finalfantasy/images/5/58/FFXV_Ignis.jpg", "Ignis")
        };
        dataset = Arrays.asList(imgs);
        imageSrcs = new ArrayList<>();
        for (CarouselData img : dataset) {
            imageSrcs.add(img.image_url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getView());

        mCarousel.setDataset(imageSrcs, dataset, new CarouselView.OnCarouselClicked<CarouselData>() {
            @Override
            public void onClick(CarouselData data) {
                Toast.makeText(getActivity(), data.caption, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CarouselData {
        private String image_url;
        private String caption;

        CarouselData(String url, String caption) {
            this.image_url = url;
            this.caption = caption;
        }
    }
}
