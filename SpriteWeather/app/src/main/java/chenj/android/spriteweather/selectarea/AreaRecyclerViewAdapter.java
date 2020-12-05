package chenj.android.spriteweather.selectarea;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import chenj.android.spriteweather.R;


/**
 * 地区显示列表
 * Created by Administrator on 2017/11/16 0016.
 */

public class AreaRecyclerViewAdapter extends RecyclerView.Adapter<AreaRecyclerViewAdapter.ViewHolder> {
    private List<String> area_list;
    private RecyclerViewListener listener;
    public AreaRecyclerViewAdapter(List<String> area_list,RecyclerViewListener listener) {
        this.listener = listener;
        this.area_list = area_list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView selected_area;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.area_name_tv);
            selected_area = (CardView)itemView.findViewById(R.id.selected_area);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item,parent,false );
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        String name = area_list.get(position);
        holder.textView.setText(name);

        holder.selected_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return area_list.size();
    }

}
