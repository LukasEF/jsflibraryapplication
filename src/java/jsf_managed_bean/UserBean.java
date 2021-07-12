package jsf_managed_bean;

import command.CommandFactory;
import dto.BookDTO;
import dto.CopyDTO;
import dto.LibrarianDTO;
import dto.LoanDTO;
import dto.MemberDTO;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author gdm1
 */
@Named(value = "user")
@SessionScoped
public class UserBean implements Serializable
{

    private static final int MEMBER = 1;
    private static final int LIBRARIAN = 2;

    private String password;
    private String username;
    private int userType;

    private BookDTO bookToDisplay;
    private MemberDTO member;
    private LibrarianDTO librarian;
    private int numCopiesToAdd;
    private boolean referenceOnly;

    public UserBean()
    {
        clearUserDetails();
    }

    public String addCopies()
    {
        CommandFactory
                .createCommand(
                        CommandFactory.CREATE_COPIES_FOR_BOOK,
                        bookToDisplay.getId(),
                        numCopiesToAdd,
                        referenceOnly)
                .execute();

        bookToDisplay.getCopies().clear();

        bookToDisplay
                = (BookDTO) CommandFactory
                        .createCommand(
                                CommandFactory.ADD_COPIES_TO_BOOK, 
                                bookToDisplay)
                        .execute();
        return "librarianBookDetails";
    }
    
    //METHOD NOT ORIGINALLY IN SAMPLE LIBRARY WEB APPLICATION
    public void createCopiesRest(int bookid, int numberofcopies, boolean referenceonly)
    {
        CommandFactory.createCommand(CommandFactory.CREATE_COPIES_FOR_BOOK,
                                     bookid,
                                     numberofcopies,
                                     referenceonly)
                      .execute();
    }

    public String borrowCopy(CopyDTO copy)
    {
        CommandFactory
                .createCommand(
                        CommandFactory.BORROW_BOOK, 
                        member.getId(), 
                        copy.getId())
                .execute();
        return "memberMain";
    }
    
    //METHOD NOT ORIGINALLY IN SAMPLE LIBRARY WEB APPLICATION
    public void borrowCopyRest(int memberid, int copyid)
    {
        CommandFactory
                .createCommand(
                        CommandFactory.BORROW_BOOK, 
                        memberid, 
                        copyid)
                .execute();
    }

    public String checkCredentials()
    {
        switch (userType)
        {
            case MEMBER:
                member = (MemberDTO) CommandFactory
                        .createCommand(
                                CommandFactory.CHECK_MEMBER_CREDENTIALS, 
                                username, 
                                password)
                        .execute();
                if (member != null)
                {
                    return "memberMain";
                }
                break;
            case LIBRARIAN:
                librarian = (LibrarianDTO) CommandFactory
                        .createCommand(
                                CommandFactory.CHECK_LIBRARIAN_CREDENTIALS, 
                                username, 
                                password)
                        .execute();
                if (librarian != null)
                {
                    return "librarianMain";
                }
                break;
            default:
                break;
        }

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null, 
                        new FacesMessage("Login credentials are not correct"));
        
        return "";
    }

    private void clearUserDetails()
    {
        username = "";
        password = "";
        userType = MEMBER;
        bookToDisplay = null;
        member = null;
        librarian = null;
        numCopiesToAdd = 0;
        referenceOnly = false;
    }

    public String displayBook(BookDTO book)
    {
        bookToDisplay = (BookDTO) CommandFactory
                .createCommand(
                        CommandFactory.ADD_COPIES_TO_BOOK, 
                        book)
                .execute();
        return member != null ? "bookDetails" : "librarianBookDetails";
    }
    
    //METHOD NOT ORIGINALLY IN SAMPLE LIBRARY WEB APPLICATION
    public BookDTO displayBookRest(BookDTO bookfromJson)
    {
        BookDTO output = (BookDTO) CommandFactory
                .createCommand(
                        CommandFactory.ADD_COPIES_TO_BOOK, 
                        bookfromJson)
                .execute();
        return output;
    }

    public ArrayList<BookDTO> getAllBooks()
    {
        return (ArrayList<BookDTO>) CommandFactory
                .createCommand(
                        CommandFactory.ALL_BOOKS)
                .execute();
    }

    public BookDTO getBookToDisplay()
    {
        return bookToDisplay;
    }

    public ArrayList<LoanDTO> getCurrentLoansForMember()
    {
        return (ArrayList<LoanDTO>) CommandFactory
                .createCommand(
                        CommandFactory.CURRENT_LOANS_FOR_MEMBER, 
                        member)
                .execute();
    }
    
    //METHOD NOT ORIGINALLY IN SAMPLE LIBRARY WEB APPLICATION
    public ArrayList<LoanDTO> getCurrentLoansForMemberRest(MemberDTO mem)
    {
        return (ArrayList<LoanDTO>) CommandFactory
                .createCommand(
                        CommandFactory.CURRENT_LOANS_FOR_MEMBER, 
                        mem)
                .execute();
    }

    public LibrarianDTO getLibrarian()
    {
        return librarian;
    }

    public ArrayList<LoanDTO> getLoanHistoryForMember()
    {
        return (ArrayList<LoanDTO>) CommandFactory
                .createCommand(
                        CommandFactory.LOAN_HISTORY_FOR_MEMBER, 
                        member.getId())
                .execute();
    }
    
    //METHOD NOT ORIGINALLY IN SAMPLE LIBRARY WEB APPLICATION
    public ArrayList<LoanDTO> getLoanHistoryForMemberRest(MemberDTO m)
    {
        return (ArrayList<LoanDTO>) CommandFactory
                .createCommand(
                        CommandFactory.LOAN_HISTORY_FOR_MEMBER, 
                        m.getId())
                .execute();
    }
    
    public MemberDTO getMember()
    {
        return member;
    }

    public int getNumberOfCopiesToAdd()
    {
        return numCopiesToAdd;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUsername()
    {
        return username;
    }

    public int getUserType()
    {
        return userType;
    }

    public boolean isReferenceOnly()
    {
        return referenceOnly;
    }

    public String logout()
    {
        clearUserDetails();
        return "index";
    }

    public String renewLoan(LoanDTO loan)
    {
        CommandFactory
                .createCommand(
                        CommandFactory.RENEW_LOAN, 
                        loan)
                .execute();
        return "";
    }

    public String returnCopy(LoanDTO loan)
    {
        CommandFactory
                .createCommand(
                        CommandFactory.RETURN_BOOK, 
                        loan)
                .execute();
        return "";
    }

    public void setNumberOfCopiesToAdd(int numCopies)
    {
        this.numCopiesToAdd = numCopies;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setReferenceOnly(boolean b)
    {
        referenceOnly = b;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }
}
