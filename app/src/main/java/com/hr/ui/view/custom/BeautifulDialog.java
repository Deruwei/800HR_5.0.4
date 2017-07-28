package com.hr.ui.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hr.ui.R;


/**
 * 作者：Colin
 * 日期：2016/6/15 16:20
 * 邮箱：bestxt@qq.com
 */
public class BeautifulDialog extends Dialog {

    //实现默认构造函数
    public BeautifulDialog(Context context, int theme) {


        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    protected BeautifulDialog(Context context, boolean cancelable,
                              OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public BeautifulDialog(Context context) {
        super(context);

        // TODO Auto-generated constructor stub
    }

    //所有的方法执行完都会返回一个Builder使得后面可以直接create和show
    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;//确定按钮
        private String negativeButtonText;//取消按钮
        private View contentView;
        private BaseAdapter adapter;//listview的adapter
        //确定按钮事件
        private OnClickListener positiveButtonClickListener;
        //取消按钮事件
        private OnClickListener negativeButtonClickListener;
        //listview的item点击事件
        private AdapterView.OnItemClickListener listViewOnclickListener;

        public Builder(Context context) {
            this.context = context;
        }

        //设置消息
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置内容
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        //设置适配器
        public Builder setAdapter(BaseAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        //设置点击事件
        public Builder setOnClickListener(AdapterView.OnItemClickListener listViewOnclickListener) {
            this.listViewOnclickListener = listViewOnclickListener;
            return this;
        }

        //设置整个背景
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置确定按钮和其点击事件
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        //设置取消按钮和其事件
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        //createview方法
        public BeautifulDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 设置其风格
            final BeautifulDialog dialog = new BeautifulDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_dialog_layout, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // 设置标题
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            //设置listview的adapter如果没有就隐藏listview
//            if (adapter != null && adapter.getCount() > 0) {
//                ListView listView = (ListView) layout.findViewById(R.id.listView);
//                listView.setAdapter(adapter);
//                if (listViewOnclickListener != null) {
//
//                    listView.setOnItemClickListener(listViewOnclickListener);
//                }
//
//            } else {
//                layout.findViewById(R.id.listView).setVisibility(
//                        View.GONE);
//            }


            //设置确定按钮
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.beautifuldialog_positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.beautifuldialog_positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // 如果没有确定按钮就将其隐藏
                layout.findViewById(R.id.beautifuldialog_positiveButton).setVisibility(
                        View.GONE);
            }
            // 设置取消按钮
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.beautifuldialog_negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.beautifuldialog_negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // 如果没有取消按钮就将其隐藏
                layout.findViewById(R.id.beautifuldialog_negativeButton).setVisibility(
                        View.GONE);
            }
            // 设置内容
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // 添加view
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView,
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT,
                                        LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
}