package universe02.example.zhouyu.tictactoe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Zhou Yu on 2018/5/16.
 */

public class SquareButton extends Button {

    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int WidthSpec, int HeightSpec){
        int newHeightSpec = WidthSpec ;

        super.onMeasure(WidthSpec, newHeightSpec);
    }}
