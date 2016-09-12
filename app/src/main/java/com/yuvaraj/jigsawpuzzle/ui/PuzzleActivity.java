package com.yuvaraj.jigsawpuzzle.ui;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.yuvaraj.jigsawpuzzle.R;
import com.yuvaraj.jigsawpuzzle.adapters.PuzzleListAdapter;
import com.yuvaraj.jigsawpuzzle.pojos.Pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by CIPL0349 on 9/5/2016.
 */
public class PuzzleActivity extends AppCompatActivity {

    ListView listView;
    RelativeLayout relativeLayout;
    ScrollView scrollView;

    PuzzleListAdapter puzzleListAdapter;

    List<Pieces> piecesModelListMain = new ArrayList<Pieces>();

    HashMap<String, Pieces> piecesModelHashMap = new HashMap<String, Pieces>();
    List<Pieces> removedPiecesModelList = new ArrayList<Pieces>();

    int countGrid = 0;

    //Set this based on resourceImages
    int WIDTH = 144;

    int[] resourceImages = new int[]{R.drawable.ic_1,
            R.drawable.ic_2,
            R.drawable.ic_3, R.drawable.ic_4,
            R.drawable.ic_5,
            R.drawable.ic_6, R.drawable.ic_7,
            R.drawable.ic_8, R.drawable.ic_9};

    List<Integer> randomList = new ArrayList<Integer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_activity);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnDragListener(new MyDragListener(null));

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setOnDragListener(new MyDragListener(null));

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setOnDragListener(new MyDragListener(null));

        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            randomList.add(random.nextInt(8));
            recursive(random.nextInt(8), i);
        }

        

        RelativeLayout.LayoutParams params;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(getPixel(WIDTH * j), getPixel(WIDTH * i), 0, 0);
                final ImageView button2 = new ImageView(this);
                button2.setId(generateViewId());
                button2.setTag("" + countGrid);
                if (randomList.contains(countGrid)) {
                    //set some dummy sample image
                    button2.setImageResource(R.drawable.sampleimage);
                } else {
                    button2.setImageResource(resourceImages[countGrid]);
                }
                button2.setOnDragListener(new MyDragListener(button2));
                button2.setLayoutParams(params);
                relativeLayout.addView(button2);

                Pieces piecesModel = new Pieces();
                piecesModel.setpX(i);
                piecesModel.setpY(j);
                piecesModel.setPosition(countGrid);
                piecesModel.setOriginalResource(resourceImages[countGrid]);
                piecesModelListMain.add(piecesModel);
                piecesModelHashMap.put(i + "," + j, piecesModel);
                piecesModel = null;

                countGrid++;

            }
        }

        for (int i = 0; i < randomList.size(); i++) {
            removedPiecesModelList.add(piecesModelListMain.get(randomList.get(i)));
        }

        puzzleListAdapter = new PuzzleListAdapter(this, removedPiecesModelList);
        listView.setAdapter(puzzleListAdapter);
        puzzleListAdapter.notifyDataSetChanged();


    }

    public void recursive(int value, int i) {
        Random random = new Random();

        if (randomList.contains(value)) {
            value = random.nextInt(8);
            recursive(value, i);
        } else {
            randomList.set(i, value);
        }

    }

    public int getPixel(int dp) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    public int generateViewId() {
        final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }


    static public class MyClickListener implements View.OnLongClickListener {

        // called when the item is long-clicked
        @Override
        public boolean onLongClick(View view) {
            // TODO Auto-generated method stub

            // create it from the object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, //data to be dragged
                    shadowBuilder, //drag shadow
                    view, //local data about the drag and drop operation
                    0   //no needed flags
            );

            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }


    public class MyDragListener implements View.OnDragListener {

        final ImageView imageView;

        public MyDragListener(final ImageView imageView) {
            this.imageView = imageView;
        }


        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Handles each of the expected events
            switch (event.getAction()) {

                //signal for the start of a drag and drop operation.
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;

                //the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundResource(R.drawable.target_shape);    //change the shape of the view
                    break;

                //the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundResource(R.drawable.normal_shape);    //change the shape of the view back to normal
                    break;

                //drag shadow has been released,the drag point is within the bounding box of the View
                case DragEvent.ACTION_DROP:
                    //v is the dynamic grid imageView, we accept the drag item
                    //view is listView imageView the dragged item
                    if (v == imageView) {
                        View view = (View) event.getLocalState();

                        ViewGroup owner = (ViewGroup) v.getParent();
                        if (owner == relativeLayout) {
                            String selectedViewTag = view.getTag().toString();

                            Pieces piecesModel = piecesModelListMain.get(Integer.parseInt(v.getTag().toString()));
                            String xy = piecesModel.getpX() + "," + piecesModel.getpY();

                            if (xy.equals(selectedViewTag)) {
                                ImageView imageView = (ImageView) v;
                                imageView.setImageResource(piecesModelListMain.get(Integer.parseInt(v.getTag().toString())).getOriginalResource());
                                piecesModel = null;
                                Toast.makeText(getApplicationContext(), "The correct Puzzle", Toast.LENGTH_LONG).show();
                            } else {
                                piecesModel = null;
                                view.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Not the correct Puzzle", Toast.LENGTH_LONG).show();
                                break;
                            }
                        } else {
                            View view1 = (View) event.getLocalState();
                            view1.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "You can't drop the image here", Toast.LENGTH_LONG).show();
                            break;
                        }
                    } else if (v == scrollView) {
                        View view1 = (View) event.getLocalState();
                        view1.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "You can't drop the image here", Toast.LENGTH_LONG).show();
                        break;
                    } else if (v == listView) {
                        View view1 = (View) event.getLocalState();
                        view1.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "You can't drop the image here", Toast.LENGTH_LONG).show();
                        break;
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "You can't drop the image here", Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;

                //the drag and drop operation has concluded.
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundResource(R.drawable.normal_shape);	//go back to normal shape

                default:
                    break;
            }

            return true;
        }
    }


}
