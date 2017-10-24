package stevica.com.lottieexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.airbnb.lottie.LottieAnimationView;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.core.LockableViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends IntroActivity {

    private float[] ANIMATION_TIMES = {0f, 0.3333f, 0.6666f, 1f, 1f};

    private LottieAnimationView animationView;
    private LockableViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animationView = (LottieAnimationView) View.inflate(this, R.layout.app_intro_animation_view, null);
        viewPager = (LockableViewPager) findViewById(R.id.intro_activity_viewPager);

        setViewPagerScroller();
        getRootView().addView(animationView, 0);

        addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float startProgress = ANIMATION_TIMES[position];
                float endProgress = ANIMATION_TIMES[position + 1];

                animationView.setProgress(startProgress + positionOffset * (endProgress - startProgress));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected Collection<? extends Fragment> generatePages(Bundle savedInstanceState) {
        List<EmptyFragment> list = new ArrayList<>();
        list.add(EmptyFragment.newInstance());
        list.add(EmptyFragment.newInstance());
        list.add(EmptyFragment.newInstance());
        list.add(EmptyFragment.newInstance());
        return list;
    }

    @Override
    protected IntroButton.Behaviour generateFinalButtonBehaviour() {
        return new IntroButton.Behaviour() {
            @Override
            public void setActivity(IntroActivity activity) {
                finish();
            }

            @Override
            public IntroActivity getActivity() {
                return null;
            }

            @Override
            public void run() {

            }
        };
    }

    private void setViewPagerScroller() {

        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            Scroller scroller = new Scroller(this, (Interpolator) interpolator.get(null)) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, duration * 7);
                }
            };
            scrollerField.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
            // Do nothing.
            Log.d("SMEDIC", "exception!");
        }
    }
}
