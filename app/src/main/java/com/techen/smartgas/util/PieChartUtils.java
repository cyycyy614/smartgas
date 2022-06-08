package com.techen.smartgas.util;

import android.graphics.Color;
import android.graphics.Matrix;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.techen.smartgas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 饼图
 */
public class PieChartUtils {

    public static PieChart initChart(PieChart pieChart, boolean isXEnbale, boolean isLeftYEnable, boolean isRightYEnable) {
        //饼状图
        pieChart.setUsePercentValues(true);//设置value是否用显示百分数,默认为false
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(20f, 15f, 20f, 15f);//设置饼状图距离上下左右的偏移量

        pieChart.setDragDecelerationFrictionCoef(0.45f);//设置阻尼系数,范围在[0,1]之间,越小饼状图转动越困难
        //设置中间文字
        pieChart.setDrawCenterText(false);//是否绘制中间的文字
        pieChart.setCenterText("");//设置饼中间标题
        pieChart.setCenterTextSizePixels(10);
        pieChart.setCenterTextColor(Color.parseColor("#292F4C")); //中间问题的颜色
        pieChart.setCenterTextSize(15f);//中间文字的大小px
//        pieChart.setCenterTextTypeface(mTfLight);       //设置PieChart内部圆文字的字体样式

        pieChart.setNoDataText("暂无数据");// 如果没有数据的时候，会显示这个，类似ListView的EmptyView
//内部圆环
        pieChart.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
        pieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色

        pieChart.setTransparentCircleColor(Color.WHITE);//设置圆环的颜色
        pieChart.setTransparentCircleAlpha(220);//设置圆环的透明度[0,255]数值越小越透明

        pieChart.setHoleRadius(55f);//饼状图中间的圆的半径大小
        pieChart.setTransparentCircleRadius(5f);//设置圆环的半径值

        pieChart.setRotationAngle(0f);//设置饼状图旋转的角度
        // 触摸旋转
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);//设置旋转的时候点中的tab是否高亮(默认为true)
        // 输入标签样式
        pieChart.setDrawEntryLabels(false);//是否绘制饼上的字体（是否显示每个部分的文字描述），false只显示百分比，
        pieChart.setEntryLabelColor(Color.BLACK);//描述文字的颜色
//        pieChart.setEntryLabelTypeface(mTfRegular);  //描述文字的样式
        pieChart.setEntryLabelTextSize(10f);//描述文字的大小

        pieChart.animateX(50);

        //设置每个tab比例块的显示位置(饼图外字体)
        Legend l = pieChart.getLegend();//设置比例块  饼图外数据的位置
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);//图例竖直居中
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);//图例水平右对齐
        l.setOrientation(Legend.LegendOrientation.VERTICAL) ;//设置图例垂直排列
        l.setDrawInside(false); //
        l.setXEntrySpace(5f);//设置tab比例块之间X轴方向上的空白间距值(水平排列时)
        l.setYEntrySpace(5f);//设置tab比例块之间Y轴方向上的空白间距值(垂直排列时)
        l.setYOffset(0f);//设置比例块Y轴偏移量
        l.setFormSize(10f);//设置比例块大小
        l.setForm(Legend.LegendForm.CIRCLE);//设置图例形状，默认为方块SQUARE，CIRCLE圆角
        l.setFormToTextSpace(10f);           //设置每个图例实体中标签和形状之间的间距
        l.setTextSize(12f);//设置比例块字体大小
        l.setTextColor(Color.parseColor("#000000"));
        l.setEnabled(true);//设置是否启用比例块,默认启用
        l.setWordWrapEnabled(false);//设置比例块换行...
        // 不显示图例
//        l.setEnabled(false);

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);
//        chart.setEntryLabelTypeface(tfRegular);

        pieChart.setEntryLabelTextSize(14f);
        pieChart.setNoDataTextColor(Color.parseColor("#999999"));
        pieChart.setNoDataText("你还没有记录数据");
        pieChart.invalidate();
        return pieChart;
    }


    /**
     * 更新图表
     *
     * @param chart  图表
     * @param values 数据点
     */
    public static void notifyDataSetChanged(PieChart chart, List<PieEntry> values) {
        chart.invalidate();
        setChartData(chart, values);
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values xy数据集合
     */
    public static void setChartData(PieChart chart, List<PieEntry> values) {
        ArrayList<PieEntry> entries = new ArrayList<>();

//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//        for (int i = 0; i < count ; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
//                    parties[i % parties.length],
//                    getResources().getDrawable(R.drawable.star)));
//        }

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#6AE5B0"));
        colors.add(Color.parseColor("#FD754B"));
        colors.add(Color.parseColor("#845FFD"));
        colors.add(Color.parseColor("#24C4D4"));
        colors.add(Color.parseColor("#FFB03F"));

        PieDataSet dataSet = new PieDataSet(values, "");

        dataSet.setSliceSpace(0f);//饼子间距
        dataSet.setSelectionShift(3f);//点击某个饼子伸长。设置饼状Item被选中时变化的距离
        dataSet.setColors(colors); //为DataSet中的数据匹配上颜色集(饼图Item颜色)

        //设置折线
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        //设置线的长度
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.5f);
        //设置文字和数据图外显示
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());  //设置所有DataSet内数据实体（百分比）的文本字体格式
        data.setValueTextSize(13f);//设置所有DataSet内数据实体（百分比）的文本字体大小
        data.setValueTextColor(Color.BLACK); //设置所有DataSet内数据实体（百分比）的文本颜色

        // add a lot of colors

//        ArrayList<Integer> colors = new ArrayList<>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();


    }

    /**
     * 显示无数据的提示
     *
     * @param chart
     */
    public static void NotShowNoDataText(Chart chart) {
        chart.clear();
        chart.notifyDataSetChanged();
        chart.setNoDataTextColor(Color.parseColor("#999999"));
        chart.setNoDataText("你还没有记录数据");
        chart.invalidate();
    }
}
