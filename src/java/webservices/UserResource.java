/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices;

import com.google.gson.Gson;
import dto.BookDTO;
import dto.CopyDTO;
import dto.LoanDTO;
import dto.MemberDTO;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import jsf_managed_bean.UserBean;

/**
 * REST Web Service
 *
 * @author lukas
 */
@Path("user")
public class UserResource {

    @Context
    private UriInfo context;
    private UserBean ub = new UserBean();

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUserJSON(String userInfo) {
        try {
            ub = new Gson().fromJson(userInfo, UserBean.class);
            if(ub.checkCredentials().equalsIgnoreCase("memberMain") || ub.checkCredentials().equalsIgnoreCase("librarianMain"))
            {
                return new Gson().toJson(ub, UserBean.class);
            }
        } catch (NullPointerException npe) {
        }
        return "{}";
    }
    
    @POST
    @Path("/createCopies/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createCopies(String copy)
    {
        String bookid;
        String numberofcopies;
        String reference = null;
        
        if(!copy.contains("bookid") || !copy.contains("numberofcopies") || !copy.contains("referenceonly"))
        {
            return "Failure";
        }
        else
        {
            int colon1 = copy.indexOf(":");
            int colon2 = copy.indexOf(":", colon1+1);
            int comma1 = copy.indexOf(",");
            int comma2 = copy.indexOf(",", comma1+1);
            bookid = copy.substring(colon1+1, comma1);
            numberofcopies = copy.substring(colon2+1, comma2);
        }
        
        if(copy.contains("true"))
        {
            int refIndex = copy.indexOf("true");
            reference = copy.substring(refIndex, refIndex+4);
        }
        else if(copy.contains("false"))
        {
            int refIndex = copy.indexOf("false");
            reference = copy.substring(refIndex, refIndex+5);
        }
        
        int bookIDNum = Integer.parseInt(bookid);
        int copiesNum = Integer.parseInt(numberofcopies);
        boolean ref = Boolean.parseBoolean(reference);
        
        ub.createCopiesRest(bookIDNum, copiesNum, ref);
        return "Success! Added " + numberofcopies + " copies";
    }
    
    @POST
    @Path("/borrowBook")
    @Consumes(MediaType.APPLICATION_JSON)
    public void borrowBook(String json)
    {
        int copyid = Integer.parseInt(json.substring(json.indexOf("copyid")+8, json.indexOf("memberid")-2));
        int memberid = Integer.parseInt(json.substring(json.indexOf("memberid")+10, json.length()-1));
        
        ub.borrowCopyRest(memberid, copyid);
    }
    
    @GET
    @Path("/allBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<BookDTO> getAllBooks() 
    {
        return ub.getAllBooks();
    }
    
    @GET
    @Path("/getCopies/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<CopyDTO> getCopies(@PathParam("id") int id)
    {
        ArrayList<CopyDTO> output = new ArrayList<CopyDTO>();
        try
        {
            BookDTO b = new BookDTO(id, "Title", "Author", "ISBN");
            b = ub.displayBookRest(b);
            for(int i = 0; i < b.getNumberOfCopies(); i++)
            {
                b.getCopies().get(i).setBookNull();
            }
            output = b.getCopies();
        }
        catch(NullPointerException e)
        {
            return null;
        }
        return output;
    }
    
    @GET
    @Path("/getLoans/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<LoanDTO> getLoans(@PathParam("id") int id)
    {
        ArrayList<LoanDTO> output = new ArrayList<LoanDTO>();
        try
        {
            MemberDTO m = new MemberDTO(id, null, null, null);
            output = ub.getCurrentLoansForMemberRest(m);
        }
        catch(NullPointerException npe)
        {
            return null;
        }
        return output;
    }
    
    @GET
    @Path("/getLoanHistory/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<LoanDTO> getLoanHistory(@PathParam("id") int id)
    {
        ArrayList<LoanDTO> output = new ArrayList<LoanDTO>();
        try
        {
            MemberDTO m = new MemberDTO(id, null, null, null);
            output = ub.getLoanHistoryForMemberRest(m);
        }
        catch(NullPointerException npe)
        {
            return null;
        }
        return output;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/renewLoan/")
    public LoanDTO renewLoan(String loan) 
    {
        int colon1 = loan.indexOf(":");
        int colon2 = loan.indexOf(":", colon1+1);
        int comma = loan.indexOf(",");
        String id = loan.substring(colon1+1, comma);
        String numrenewals = loan.substring(colon2+1, colon2+2);
        int loanid = Integer.parseInt(id);
        int loanrenewals = Integer.parseInt(numrenewals);
        LoanDTO l = new LoanDTO(loanid, null, null, null, null, null, loanrenewals);
        ub.renewLoan(l);
        
        return l;
    }
    
    @PUT
    @Path("/returnBook/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void returnBook(String json)
    {
        int loanid = Integer.parseInt(json.substring(json.indexOf("loanid")+8, json.indexOf("memberid")-2));
        int memberid = Integer.parseInt(json.substring(json.indexOf("memberid")+10, json.indexOf("copyid")-2));
        int copyid = Integer.parseInt(json.substring(json.indexOf("copyid")+8, json.indexOf("loandate")-2));
        
        String loandate = json.substring(json.indexOf("loandate")+11, json.indexOf("duedate")-34);
        Calendar loan = Calendar.getInstance();
        loan.set(Calendar.DATE, Integer.parseInt(loandate.substring(8,10)));
        loan.set(Calendar.MONTH, Integer.parseInt(loandate.substring(5,7)));
        loan.set(Calendar.YEAR, Integer.parseInt(loandate.substring(0,4)));
        
        String duedate = json.substring(json.indexOf("duedate")+10, json.indexOf("numrenewals")-34);
        Calendar due = Calendar.getInstance();
        due.set(Calendar.DATE, Integer.parseInt(duedate.substring(8,10)));
        due.set(Calendar.MONTH, Integer.parseInt(duedate.substring(5,7)));
        due.set(Calendar.YEAR, Integer.parseInt(duedate.substring(0,4)));
        
        int numrenewals = Integer.parseInt(json.substring(json.indexOf("numrenewals")+13, json.indexOf("numrenewals")+14));
        
        MemberDTO m = new MemberDTO(memberid, null, null, null);
        CopyDTO c = new CopyDTO(copyid, null, false, false);
        LoanDTO l = new LoanDTO(loanid, m, c, loan, due, null, numrenewals);
        
        ub.returnCopy(l);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) 
    {
        
    }
    
}