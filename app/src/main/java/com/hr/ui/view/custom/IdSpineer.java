package com.hr.ui.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * 作者：Colin
 * 日期：2016/1/18 14:42
 * 邮箱：bestxt@qq.com
 * 自定义spinner 可获取item对应id
 */
public class IdSpineer extends Spinner {

    public String idString;
    public String[] idStrings;

    public IdSpineer(Context context) {
        super(context);
    }

    public IdSpineer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }

    // 如果视图定义了OnClickListener监听器，调用此方法来执行
    @Override
    public boolean performClick() {
        // TODO Auto-generated method stub
        return super.performClick();
    }

    /*
     * 给每一项设置id
     */
    public void setIds(String[] idStrings) {
        this.idStrings = idStrings;
        this.idString = idStrings[0];
    }
    /*
     * 返回所选项对应id
     */
    public String getSelectedId() {
        return this.idString;
    }

    /*
     * 设置默认选中项
     */
    public void setSelectedItem(String idString) {
        //System.out.println("##"+idString);
        try {
            if (idString == null) {
                this.idString = null;
            } else {
                for (int i = 0; i < idStrings.length; i++) {
                    if (idString.equalsIgnoreCase(idStrings[i])) {
                        setSelection(i, true);
                        break;
                    }
                }
                // int index=Integer.parseInt(idString);
                // setSelection(index,true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
