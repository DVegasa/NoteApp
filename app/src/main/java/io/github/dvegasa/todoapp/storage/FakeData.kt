package io.github.dvegasa.todoapp.storage

import io.github.dvegasa.todoapp.data_models.FileInfo
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 21.08.2019
 */
class FakeData : NoteStorage {

    private val list = arrayListOf(
        Note("BOOM! Bro <3", "Мир умер. Вокруг лишь покой и тишина. Повсюду лежит снег. Иногда на эти тихие холмы налетает ветер. В эти моменты начинается метель. Лишь те, кто смог спрятаться где-то выживут в эту суровую пору. Среди холмов стоит есть несколько домов, в которых и прячутся люди от снега и холода. Но и внутри этих строений не найти настоящего тепла. Горит огонь в очаге, вот только внутри каждому зябко и холодно. И не хватает чего-то. Поэтому люди и покидают эти дома. Отправляются вдаль, на поиски того самого. Того, что способно избавить от холода внутри.\n" +
                "А где-то среди заснеженных холмов остались отпечатки маленьких лапок...", 1, arrayListOf("work", "cv"), emptyList()),
        Note("Фотографии кошек", "Мяу!", 2, arrayListOf("work", "cv"), listOf(FileInfo("", 0f, "", "", "")))
    )

    override fun getNoteById(id: Long, c: NoteStorage.Callback) {
        c.onResult(list.filter {
            it.id == id
        })
    }

    override fun getAllNotes(c: NoteStorage.Callback) {
        c.onResult(list)
    }
}