package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ads")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer price;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}
