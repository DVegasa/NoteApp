package io.github.dvegasa.todoapp.storage

import io.github.dvegasa.todoapp.data_models.FileInfo
import io.github.dvegasa.todoapp.data_models.Note

/**
 * 21.08.2019
 */
class FakeData {

    interface Callback {
        fun onResult(list: ArrayList<Note>)
    }

    private val longText = "Мир умер. Вокруг лишь покой и тишина. Повсюду лежит снег. Иногда на эти тихие холмы налетает ветер. В эти моменты начинается метель. Лишь те, кто смог спрятаться где-то выживут в эту суровую пору. Среди холмов стоит есть несколько домов, в которых и прячутся люди от снега и холода. Но и внутри этих строений не найти настоящего тепла. Горит огонь в очаге, вот только внутри каждому зябко и холодно. И не хватает чего-то. Поэтому люди и покидают эти дома. Отправляются вдаль, на поиски того самого. Того, что способно избавить от холода внутри.\n" +
            "А где-то среди заснеженных холмов остались отпечатки маленьких лапок..."

    private val list = arrayListOf(
        Note("BOOM! Bro <3", longText, 1, arrayListOf("work", "cv"),   1566259200, emptyList()),
        Note("Фотографии кошек", "Мяу!", 2, arrayListOf("кошки", "фото", "кот"), 1565740800,
            listOf(FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""))),
        Note("Привет!", longText, 3, arrayListOf("хелло_мир", "hello_world"), 1564740800, emptyList()),
        Note("Фотографии кошек и маленьких котят и щенят", "Мяу!", 4, arrayListOf("meow", "щенята"), 1563740800, listOf(FileInfo("", 0f, "", "", ""))),
        Note("Фотографии кошек и маленьких котят и щенят", "Мяу!", 5, arrayListOf("meow", "щенята"), 1563740800,
            listOf(FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", "")))
    )

    init {
        list.sortBy {
            it.lastTimeModified
        }
    }

    fun getNoteById(id: Long, c: Callback) {
        val r = (list.filter {
            it.id == id
        } as ArrayList<Note>)
        if (r.size > 1) throw Exception("Not unique IDs! Note.id: ${r[0]}")
        c.onResult(r)
    }

    fun getAllNotes(c: Callback) {
        c.onResult(list)
    }
}