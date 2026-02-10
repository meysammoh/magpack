package ir.mjm.restBeans;

import ir.mjm.DBAO.hiber.IssuepagebookmarkHiberEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.0924
 * @since 1.0
 */
public final class UserBookmarked {
  private final long userId;
  //Map of Magazine Id to Bookmarked Pages.
  //    private final Map<Integer, Map<Integer,String>> magazineToBookmarkedPagesMap = new HashMap<>();
  private final List<Bookmarked> bookmarkedList = new ArrayList<>();

  public UserBookmarked(long userId) {
    this.userId = userId;
  }

  public UserBookmarked(List<IssuepagebookmarkHiberEntity> issuepagebookmarkHiberEntities) throws Exception {
    if (issuepagebookmarkHiberEntities == null || issuepagebookmarkHiberEntities.size() == 0) {
      throw new Exception("IssuepagebookmarkHiberEntity can not be null or size equal 0.");
    }
    this.userId = issuepagebookmarkHiberEntities.get(0).getUserId();
    for (IssuepagebookmarkHiberEntity bookmark : issuepagebookmarkHiberEntities) {
      addBookmark(bookmark);
    }
  }

  private void addBookmark(IssuepagebookmarkHiberEntity issuepagebookmarkHiberEntity) {
    addBookmark(
        issuepagebookmarkHiberEntity.getMagId(),
        issuepagebookmarkHiberEntity.getPage(),
        issuepagebookmarkHiberEntity.getDescription());
  }

  public void addBookmark(Integer magId, Integer page, final String desc) {
    final Bookmarked newBookmarked = new Bookmarked(magId, page, desc);
    int bookmarkedIndex = this.bookmarkedList.indexOf(newBookmarked);
    if (bookmarkedIndex < 0) {
      this.bookmarkedList.add(newBookmarked);
    }
  }
    /*   public void addBookmark(Integer magId, Integer page,final String desc) {
        Map<Integer, String> bookmarked = this.magazineToBookmarkedPagesMap.get(magId);
        if (bookmarked == null) {
            bookmarked = new HashMap<>();
            bookmarked.put(page, desc);
            this.magazineToBookmarkedPagesMap.put(magId, bookmarked);
        } else {
            bookmarked.put(page, desc);
        }
    }*/

  public long getUserId() {
    return userId;
  }

  public List<Bookmarked> getBookmarkedList() {
    return bookmarkedList;
  }

  class Bookmarked {
    private Integer magId;
    private Integer PageNumber;
    private String description;

    public Bookmarked(Integer magId, Integer pageNumber, String description) {
      this.magId = magId;
      PageNumber = pageNumber;
      this.description = description;
    }

    public Integer getMagId() {
      return magId;
    }

    public void setMagId(Integer magId) {
      this.magId = magId;
    }

    public Integer getPageNumber() {
      return PageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
      PageNumber = pageNumber;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Bookmarked)) {
        return false;
      }

      Bookmarked that = (Bookmarked) o;

      if (!getMagId().equals(that.getMagId())) {
        return false;
      }
      return getPageNumber().equals(that.getPageNumber());

    }

    @Override
    public int hashCode() {
      int result = getMagId().hashCode();
      result = 31 * result + getPageNumber().hashCode();
      return result;
    }
  }
}

