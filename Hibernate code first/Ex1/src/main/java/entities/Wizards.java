package entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wizard_deposits")
public class Wizards {
    private long id;
    private String first_name;
    private String last_name;
    private String notes;
    private int age;
    private String magic_wand_creator;
    private short magic_wand_size;
    private String deposit_group;
    private LocalDateTime deposit_start_date;
    private double deposit_amount;
    private double deposit_interest;
    private double deposit_charge;
    private LocalDateTime deposit_expiration_date;
    private boolean is_deposit_expired;

    public Wizards(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "first_name", length = 50)
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @Column(name = "last_name", nullable = false, length = 60)
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Column(name = "notes", nullable = true, columnDefinition = "text")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Column(name = "age", nullable = false)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Column(name = "magic_wand_creator", length = 100)
    public String getMagic_wand_creator() {
        return magic_wand_creator;
    }

    public void setMagic_wand_creator(String magic_wand_creator) {
        this.magic_wand_creator = magic_wand_creator;
    }

    @Column(name = "magic_wand_size")
    public short getMagic_wand_size() {
        return magic_wand_size;
    }

    public void setMagic_wand_size(short magic_wand_size) {
        this.magic_wand_size = magic_wand_size;
    }

    @Column(name = "deposit_group", length = 20)
    public String getDeposit_group() {
        return deposit_group;
    }

    public void setDeposit_group(String deposit_group) {
        this.deposit_group = deposit_group;
    }

    @Column(name = "deposit_start_date")
    public LocalDateTime getDeposit_start_date() {
        return deposit_start_date;
    }

    public void setDeposit_start_date(LocalDateTime deposit_start_date) {
        this.deposit_start_date = deposit_start_date;
    }

    @Column(name = "deposit_amount", precision = 10, scale = 3)
    public double getDeposit_amount() {
        return deposit_amount;
    }

    public void setDeposit_amount(double deposit_amount) {
        this.deposit_amount = deposit_amount;
    }

    @Column(name = "deposit_interest")
    public double getDeposit_interest() {
        return deposit_interest;
    }

    public void setDeposit_interest(double deposit_interest) {
        this.deposit_interest = deposit_interest;
    }

    @Column(name = "deposit_charge")
    public double getDeposit_charge() {
        return deposit_charge;
    }

    public void setDeposit_charge(double deposit_charge) {
        this.deposit_charge = deposit_charge;
    }

    @Column(name = "deposit_expiration_date")
    public LocalDateTime getDeposit_expiration_date() {
        return deposit_expiration_date;
    }

    public void setDeposit_expiration_date(LocalDateTime deposit_expiration_date) {
        this.deposit_expiration_date = deposit_expiration_date;
    }

    @Column(name = "is_deposit_expired")
    public boolean isIs_deposit_expired() {
        return is_deposit_expired;
    }

    public void setIs_deposit_expired(boolean is_deposit_expired) {
        this.is_deposit_expired = is_deposit_expired;
    }

}
