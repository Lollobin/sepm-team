package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "reservedBy", referencedColumnName = "userId")
    private ApplicationUser reservedBy;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "purchasedBy", referencedColumnName = "userId")
    private ApplicationUser purchasedBy;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "seatId", referencedColumnName = "seatId", nullable = false)
    private Seat seat;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "showId", referencedColumnName = "showId", nullable = false)
    private Show show;

    @OneToMany(mappedBy = "ticket")
    private Set<BookedIn> bookedIns;

    @Override
    public String toString() {
        return "Ticket{"
            + "ticketId="
            + ticketId
            + ", reservedBy="
            + reservedBy
            + ", purchasedBy="
            + purchasedBy
            + ", seat="
            + seat
            + ", show="
            + show
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId)
            && Objects.equals(reservedBy, ticket.reservedBy)
            && Objects.equals(purchasedBy, ticket.purchasedBy)
            && Objects.equals(seat, ticket.seat)
            && Objects.equals(show, ticket.show)
            && Objects.equals(bookedIns, ticket.bookedIns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, reservedBy, purchasedBy, seat, show, bookedIns);
    }


    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public ApplicationUser getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(ApplicationUser reservedBy) {
        this.reservedBy = reservedBy;
    }

    public ApplicationUser getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(ApplicationUser purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Set<BookedIn> getBookedIns() {
        return bookedIns;
    }

    public void setBookedIns(Set<BookedIn> bookedIns) {
        this.bookedIns = bookedIns;
    }
}
