package com.springboot.datajpa.app.util.paginator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

public class PageRender<T> {
    
    private String url;
    private Page<T> page;

    private int totalDePaginas;
    private int numeroDeElementosPorPagina;
    
    private int paginaActual;
    
    private List<PageItem> paginas;
    
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<PageItem>();
        
        numeroDeElementosPorPagina = page.getSize();
        totalDePaginas = page.getTotalPages();
        paginaActual = page.getNumber() + 1;
        
        
        int desde, hasta;
        if(totalDePaginas <= numeroDeElementosPorPagina){
            desde = 1;
            hasta = totalDePaginas;
        }else{
            if(paginaActual <= numeroDeElementosPorPagina/2){
                desde = 1;
                hasta = numeroDeElementosPorPagina;
            }else if(paginaActual >= totalDePaginas - numeroDeElementosPorPagina/2){
                desde = totalDePaginas - numeroDeElementosPorPagina + 1;
                hasta = numeroDeElementosPorPagina;
            }else{
                desde = paginaActual - numeroDeElementosPorPagina / 2;
                hasta = numeroDeElementosPorPagina;
            }
        }
        
        for(int i=0; i < hasta; i++){
            paginas.add(new PageItem(desde + i, paginaActual == desde + i));
        }
        
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public int getTotalDePaginas() {
        return totalDePaginas;
    }

    public void setTotalDePaginas(int totalDePaginas) {
        this.totalDePaginas = totalDePaginas;
    }

    public int getNumeroDeElementosPorPagina() {
        return numeroDeElementosPorPagina;
    }

    public void setNumeroDeElementosPorPagina(int numeroDeElementosPorPagina) {
        this.numeroDeElementosPorPagina = numeroDeElementosPorPagina;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    public void setPaginas(List<PageItem> paginas) {
        this.paginas = paginas;
    }
    
    public boolean isFirst(){
        return page.isFirst();
    }
    
    public boolean isLast(){
       return  page.isLast();
    }
    
    public boolean hasNext(){
        return page.hasNext();
    }
    
    public boolean hasPrevious(){
        return page.hasPrevious();
    }
}
