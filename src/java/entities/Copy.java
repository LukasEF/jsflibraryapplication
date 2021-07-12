/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lukas
 */
@Entity
@Table(name = "COPY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Copy.findAll", query = "SELECT c FROM Copy c"),
    @NamedQuery(name = "Copy.findById", query = "SELECT c FROM Copy c WHERE c.id = :id"),
    @NamedQuery(name = "Copy.findByReferenceonly", query = "SELECT c FROM Copy c WHERE c.referenceonly = :referenceonly")})
public class Copy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REFERENCEONLY")
    private Boolean referenceonly;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "copyid")
    private Collection<Loanhistory> loanhistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "copyid")
    private Collection<Loan> loanCollection;
    @JoinColumn(name = "BOOKID", referencedColumnName = "ID")
    @ManyToOne
    private Book bookid;

    public Copy() {
    }

    public Copy(Integer id) {
        this.id = id;
    }

    public Copy(Integer id, Boolean referenceonly) {
        this.id = id;
        this.referenceonly = referenceonly;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getReferenceonly() {
        return referenceonly;
    }

    public void setReferenceonly(Boolean referenceonly) {
        this.referenceonly = referenceonly;
    }

    @XmlTransient
    public Collection<Loanhistory> getLoanhistoryCollection() {
        return loanhistoryCollection;
    }

    public void setLoanhistoryCollection(Collection<Loanhistory> loanhistoryCollection) {
        this.loanhistoryCollection = loanhistoryCollection;
    }

    @XmlTransient
    public Collection<Loan> getLoanCollection() {
        return loanCollection;
    }

    public void setLoanCollection(Collection<Loan> loanCollection) {
        this.loanCollection = loanCollection;
    }

    public Book getBookid() {
        return bookid;
    }

    public void setBookid(Book bookid) {
        this.bookid = bookid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Copy)) {
            return false;
        }
        Copy other = (Copy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Copy[ id=" + id + " ]";
    }
    
}
