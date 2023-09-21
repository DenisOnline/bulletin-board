package ru.bulletin_board.bulletin_board.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "heading")
//    @NotEmpty(message = "Заголовок не может быть пустым")
//    @Size(max = 30, message = "Заголовок не должен превышать 30 символов")
    private String heading;

    @Column(name = "description", columnDefinition = "text")
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @Column(name = "price")
    @Min(value = 1, message = "Цена должна быть больше 0")
    private int price;

    @Column(name = "address")
    @NotEmpty(message = "Адрес не может быть пустым")
    private String address;

    @Column(name = "phone_number")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Номер телефона должен содержать только цифры и символ '+'") //переделай, черт!
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @Column(name = "preview_image_id")
    private Long previewImageId;

    private LocalDateTime dateTime;

    @PrePersist
    private void init() {
        dateTime = LocalDateTime.now();
    }

    public void addImageToPost(Image image) {
        image.setPost(this);
        images.add(image);
    }
}
