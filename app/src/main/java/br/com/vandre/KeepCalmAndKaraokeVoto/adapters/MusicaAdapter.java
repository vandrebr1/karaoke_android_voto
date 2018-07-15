package br.com.vandre.KeepCalmAndKaraokeVoto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;
import br.com.vandre.KeepCalmAndKaraokeVoto.model.Musica;

public class MusicaAdapter extends BaseAdapter implements Filterable {

    static class ViewHolder {
        private TextView tvMusica;
        private TextView tvCantor;
        private TextView tvNacional;
    }

    private List<Musica> originalMusicas;
    private List<Musica> filtroMusicas;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private Context context;
    private ItemFilter mFilter = new ItemFilter();
    private boolean ignorarNacionalInternacional;
    private boolean nacional;

    public MusicaAdapter(Context context, List<Musica> musicas) {
        layoutInflater = LayoutInflater.from(context);
        this.originalMusicas = musicas;
        this.filtroMusicas = musicas;
        this.context = context;

    }

    @Override
    public int getCount() {
        return filtroMusicas.size();
    }

    @Override
    public Object getItem(int index) {
        return filtroMusicas.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int posicao, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_musica,
                    arg2, false);
            viewHolder = new ViewHolder();

            if (posicao % 2 == 1) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }

            viewHolder.tvMusica = convertView
                    .findViewById(R.id.tvMusicaItem);
            viewHolder.tvCantor = convertView
                    .findViewById(R.id.tvCantorItem);
            viewHolder.tvNacional = convertView
                    .findViewById(R.id.tvNacionalItem);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (posicao % 2 == 1) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        Musica musica = filtroMusicas.get(posicao);

        viewHolder.tvMusica.setText(musica.getMusica());
        viewHolder.tvCantor.setText(musica.getCantor());
        viewHolder.tvNacional.setText(musica.getNacional() == 0 ? context.getResources().getText(R.string.internacional) : context.getResources().getText(R.string.nacional));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<Musica> list = originalMusicas;

            int count = list.size();
            final List<Musica> nlist = new ArrayList<>(count);

            Musica filtrarMusica = null;

            for (int i = 0; i < count; i++) {
                filtrarMusica = list.get(i);

                if (!ignorarNacionalInternacional) {
                    if (filtrarMusica.getNacional() == (nacional == true ? 1 : 0)) {
                        if (filtrarMusica.getMusica().toLowerCase().contains(filterString) ||
                                filtrarMusica.getCantor().toLowerCase().contains(filterString)) {
                            nlist.add(filtrarMusica);
                        }
                    }
                }else{
                    if (filtrarMusica.getMusica().toLowerCase().contains(filterString) ||
                            filtrarMusica.getCantor().toLowerCase().contains(filterString)) {
                        nlist.add(filtrarMusica);
                    }
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtroMusicas = (List<Musica>) results.values;
            notifyDataSetChanged();
        }

    }

    public boolean isIgnorarNacionalInternacional() {
        return ignorarNacionalInternacional;
    }

    public void setIgnorarNacionalInternacional(boolean ignorarNacionalInternacional) {
        this.ignorarNacionalInternacional = ignorarNacionalInternacional;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }
}
