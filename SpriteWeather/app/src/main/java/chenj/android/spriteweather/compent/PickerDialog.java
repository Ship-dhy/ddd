package chenj.android.spriteweather.compent;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import chenj.android.spriteweather.R;

/**
 * 选择对话框
 */
public class PickerDialog extends Dialog {

	public PickerDialog(Context context) {
		super(context);
	}

	public PickerDialog(Context context, int theme) {
		super(context, theme);
	}

	public interface SelectedListener{
		public void onSelected(Dialog dialog,String value);
	}


    public interface CancelListener{
        public void onCancel(Dialog dialog);
    }

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private String selected;
		private String userSelected;
		private View contentView;
		private SelectedListener listener;
		private CancelListener cancelListener;
        private List<String> datalist;
        private PickerView pickerView;



		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}


		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * 设置显示的列表，若要调用setSelected，请先调用该函数
		 * @param datalist
		 * @return
		 */
        public Builder setDataList(List<String> datalist) {
            this.datalist = datalist;
            return this;
        }


        public Builder setSelected(String selected) {
            this.selected = selected;
            return this;
        }

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,SelectedListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.listener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,SelectedListener listener) {
			this.listener = listener;
			this.positiveButtonText = positiveButtonText;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,  CancelListener cancelListener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.cancelListener = cancelListener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, CancelListener cancelListener) {
			this.negativeButtonText = negativeButtonText;
            this.cancelListener = cancelListener;
			return this;
		}

		public PickerDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final PickerDialog dialog = new PickerDialog(context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_picker_layout, null);
			dialog.addContentView(layout, new LayoutParams(	LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
            ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            ((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        if(userSelected != null) {
                            listener.onSelected(dialog, userSelected);
                        } else {
                            listener.onSelected(dialog, selected);
                        }
                }
            });

			// set the cancel button
            ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
            ((Button) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cancelListener.onCancel(dialog);
                }
            });



            pickerView = (PickerView)layout.findViewById(R.id.picker_view);
            pickerView.setSelected(datalist.indexOf(selected));
            pickerView.setData(datalist);
            pickerView.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    userSelected = text;
                }
            });
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
