package ru.bulletin_board.bulletin_board.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;
    @Column(name = "name")
    private String name;
    @Column(name = "size")
    private String size;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "contentType")
    private String contentType;
    @Lob
    private byte[] bytes;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;
}
