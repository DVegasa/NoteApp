package io.github.dvegasa.todoapp.storage.fake_data

import io.github.dvegasa.todoapp.data_models.FileInfo
import io.github.dvegasa.todoapp.data_models.Note
import io.github.dvegasa.todoapp.storage.NoteStorageInterface

/**
 * 21.08.2019
 */
class FakeData : NoteStorageInterface {
    override fun getAllNotes(cb: NoteStorageInterface.Callback) {
        cb.onResult(getAllNotes())
    }

    override fun createNote(cb: NoteStorageInterface.Callback) {

    }

    override fun getNoteById(id: Long, cb: NoteStorageInterface.Callback) {
        cb.onResult(getNoteById(id))
    }

    override fun updateNote(note: Note, cb: NoteStorageInterface.Callback) {
    }

    override fun deleteNoteById(id: Long, cb: NoteStorageInterface.Callback) {
    }






    private val longText =
        "Мир умер. Вокруг лишь покой и тишина. Повсюду лежит снег. Иногда на эти тихие холмы налетает ветер. В эти моменты начинается метель. Лишь те, кто смог спрятаться где-то выживут в эту суровую пору. Среди холмов стоит есть несколько домов, в которых и прячутся люди от снега и холода. Но и внутри этих строений не найти настоящего тепла. Горит огонь в очаге, вот только внутри каждому зябко и холодно. И не хватает чего-то. Поэтому люди и покидают эти дома. Отправляются вдаль, на поиски того самого. Того, что способно избавить от холода внутри.\n" +
                "А где-то среди заснеженных холмов остались отпечатки маленьких лапок..."

    private val list = arrayListOf(
        Note("BOOM! Bro <3", longText, 1, arrayListOf("work", "cv"), 1566259200, emptyList()),
        Note(
            "Фотографии кошек", "Мяу!", 2, arrayListOf("кошки", "фото", "кот"), 1565740800,
            listOf(FileInfo("", 0f, "", "", ""), FileInfo("", 0f, "", "", ""))
        ),
        Note("Привет!", longText, 3, arrayListOf("хелло_мир", "hello_world", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир", "хелло_мир"), 1564740800, emptyList()),
        Note(
            "Фотографии кошек и маленьких котят и щенят",
            "Мяу!",
            4,
            arrayListOf("meow", "щенята"),
            1563740800,
            listOf(FileInfo("", 0f, "", "", ""))
        ),
        Note(
            "Тут можно посмотреть файлы", "Мяу!", 5, arrayListOf("meow", "щенята"), 1563740800,
            listOf(
                fakeFileInfo("Кошечка"),
                fakeFileInfo("Собачка"),
                fakeFileInfo("Собачка №2"),
                fakeFileInfo("Собачечка"),
                fakeFileInfo("Мяукалка")
            )
        )
    )

    init {
        list.sortBy {
            it.lastTimeModified
        }
    }

    fun getNoteById(id: Long): ArrayList<Note> {
        val r = (list.filter {
            it.id == id
        } as ArrayList<Note>)
        if (r.size > 1) throw Exception("Not unique IDs! Note.id: ${r[0]}")
        return r
    }

    fun getAllNotes() = list


    private fun fakeFileInfo(title: String) =
        FileInfo(title, (1..14).random().toFloat(), "png", "", "")
}