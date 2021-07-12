/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lukas
 */
@Entity
@Table(name = "LOAN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loan.findAll", query = "SELECT l FROM Loan l"),
    @NamedQuery(name = "Loan.findById", query = "SELECT l FROM Loan l WHERE l.id = :id"),
    @NamedQuery(name = "Loan.findByLoandate", query = "SELECT l FROM Loan l WHERE l.loandate = :loandate"),
    @NamedQuery(name = "Loan.findByDuedate", query = "SELECT l FROM Loan l WHERE l.duedate = :duedate"),
    @NamedQuery(name = "Loan.findByNumrenewals", query = "SELECT l FROM Loan l WHERE l.numrenewals = :numrenewals")})
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOANDATE")
    @Temporal(TemporalType.DATE)
    private Date loandate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DUEDATE")
    @Temporal(TemporalType.DATE)
    private Date duedate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMRENEWALS")
    private int numrenewals;
    @JoinColumn(name = "COPYID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Copy copyid;
    @JoinColumn(name = "MEMBERID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Member1 memberid;

    public Loan() {
    }

    public Loan(Integer id) {
        this.id = id;
    }

    public Loan(Integer id, Date loandate, Date duedate, int numrenewals) {
        this.id = id;
        this.loandate = loandate;
        this.duedate = duedate;
        this.numrenewals = numrenewals;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLoandate() {
        return loandate;
    }

    public void setLoandate(Date loandate) {
        this.loandate = loandate;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public int getNumrenewals() {
        return numrenewals;
    }

    public void setNumrenewals(int numrenewals) {
        this.numrenewals = numrenewals;
    }

    public Copy getCopyid() {
        return copyid;
    }

    public void setCopyid(Copy copyid) {
        this.copyid = copyid;
    }

    public Member1 getMemberid() {
        return memberid;
    }

    public void setMemberid(Member1 memberid) {
        this.memberid = memberid;
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
        if (!(object instanceof Loan)) {
            return false;
        }
        Loan other = (Loan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Loan[ id=" + id + " ]";
    }
    
}
