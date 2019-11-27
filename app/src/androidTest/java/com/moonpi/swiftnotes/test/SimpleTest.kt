package com.moonpi.swiftnotes.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.widget.ImageButton
import android.widget.TextView
import com.moonpi.swiftnotes.MainActivity
import com.moonpi.swiftnotes.R
import com.moonpi.swiftnotes.rule.SwiftnotesRule
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.tinkoff.allure.android.deviceScreenshot
import ru.tinkoff.allure.annotations.DisplayName
import ru.tinkoff.allure.step

@RunWith(AndroidJUnit4::class)
@DisplayName("Создание заметки")
class SimpleTest : AbstractSwiftnotesTest() {

    @get:Rule
    val rule = SwiftnotesRule(MainActivity::class.java, false)

    @Test
    @DisplayName("Проверка открытия страницы создания")
    fun newNoteHints() {
        rule.launchActivity()
        step("Проверяем отображение страницы") {
            onView(withId(R.id.newNote)).perform(click())
            onView(allOf(withId(R.id.titleEdit), isDisplayed())).check(matches(withHint("Title")))
            onView(allOf(withId(R.id.bodyEdit), isDisplayed())).check(matches(withHint("Note")))
            deviceScreenshot("page_display")
        }
    }

    // 1. Проверка экрана создания заметки
    @Test
    @DisplayName("Проверка экрана создания заметки")
    fun addNoteScreenTest() {
        rule.launchActivity()
        step("Проверка отображения экрана создания заметки") {
            onView(withId(R.id.newNote)).perform(click())
            onView(allOf(withId(R.id.titleEdit), isDisplayed())).check(matches(withHint("Title")))
            onView(allOf(withId(R.id.bodyEdit), isDisplayed())).check(matches(withHint("Note")))
            deviceScreenshot("add_note_page_display")
        }
        step("Проверка отображения диалогового окна") {
            onView(allOf(withClassName(containsString(ImageButton::class.java.simpleName)), withParent(withId(R.id.toolbarEdit)))).perform(click())
            onView(allOf(withId(android.R.id.message), isDisplayed())).check(matches(withText("Save changes?")))
            onView(allOf(withId(android.R.id.button2), isDisplayed())).check(matches(withText("NO")))
            onView(allOf(withId(android.R.id.button1), isDisplayed())).check(matches(withText("YES")))
            deviceScreenshot("dialog_save_display")
        }
        step("Проверка отображения начального экрана") {
            onView(withId(android.R.id.button2)).perform(click())
            onView(allOf(withClassName(containsString(TextView::class.java.simpleName)), withParent(withId(R.id.toolbarMain)), isDisplayed())).check(matches(withText("Swiftnotes")))
            deviceScreenshot("main_page_display")
        }
    }

    // 2. Проверка сохранения заметки
    @Test
    @DisplayName("Проверка сохранения заметки")
    fun saveNoteTest() {
        val noteTitle = "New note title"
        val noteText = "New note text"

        rule.launchActivity()
        step("Открытие экрана создания заметки") {
            onView(withId(R.id.newNote)).perform(click())
            deviceScreenshot("add_note_page_display")
        }
        step("Создание новой заметки") {
            onView(allOf(withId(R.id.titleEdit), isDisplayed())).perform(typeText(noteTitle))
            onView(allOf(withId(R.id.bodyEdit), isDisplayed())).perform(typeText(noteText))
            onView(allOf(withId(R.id.titleEdit), isDisplayed())).check(matches(withText(noteTitle)))
            onView(allOf(withId(R.id.bodyEdit), isDisplayed())).check(matches(withText(noteText)))
            deviceScreenshot("new_note")
        }
        step("Сохранение заметки") {
            onView(allOf(withClassName(containsString(ImageButton::class.java.simpleName)), isDisplayed())).perform(click())
            deviceScreenshot("dialog_save_display")
            onView(allOf(withId(android.R.id.button1), isDisplayed())).perform(click())
        }
        step("Проверка отображения заметки на главном экране") {
            onView(withId(R.id.titleView)).check(matches(allOf(withText(noteTitle), isDisplayed())))
            onView(withId(R.id.bodyView)).check(matches(allOf(withText(noteText), isDisplayed())))
            deviceScreenshot("new_note_on_main_page_display")
        }
    }

    // 3. Проверка пунктов меню в тулбаре
    @Test
    @DisplayName("Проверка пунктов меню в тулбаре")
    fun checkToolbarMenuTest() {
        val backupNotesText = "Backup notes"
        val restoreNotesText = "Restore notes"
        val rateAppText = "Rate app"
        val noteFontSizeText = "Note font size"
        val hideNoteBodyText = "Hide note body in list"

        rule.launchActivity()

        step("Проверка отображения меню на главном экране") {
            onView(withContentDescription("Ещё")).perform(click())
            onView(allOf(withText(R.string.action_backup), isDisplayed())).check(matches(withText(backupNotesText)))
            onView(allOf(withText(R.string.action_restore), isDisplayed())).check(matches(withText(restoreNotesText)))
            onView(allOf(withText(R.string.action_rate_app), isDisplayed())).check(matches(withText(rateAppText)))
            deviceScreenshot("menu_on_main_page_display")
        }
        Espresso.pressBack()
        step("Открытие экрана создания заметки") {
            onView(withId(R.id.newNote)).perform(click())
            deviceScreenshot("add_note_page_display")
        }
        step("Проверка отображения меню на экране создания заметки") {
            onView(withContentDescription("Ещё")).perform(click())
            onView(allOf(withText(R.string.action_font_size), isDisplayed())).check(matches(withText(noteFontSizeText)))
            onView(allOf(withText(R.string.action_hide_body), isDisplayed())).check(matches(withText(hideNoteBodyText)))
            deviceScreenshot("menu_on_edit_page_display")
        }
    }

    // 4. Проверка удаления заметки
    @Test
    @DisplayName("Проверка удаления заметки")
    fun deleteNoteTest() {
        val noteTitle = "New note title"
        val noteText = "New note text"

        rule.launchActivity()
        step("Открытие экрана создания заметки") {
            onView(withId(R.id.newNote)).perform(click())
            deviceScreenshot("add_note_page_display")
        }
        step("Создание новой заметки") {
            onView(allOf(withId(R.id.titleEdit), isDisplayed())).perform(typeText(noteTitle))
            onView(allOf(withId(R.id.bodyEdit), isDisplayed())).perform(typeText(noteText))
            deviceScreenshot("new_note")
        }
        step("Сохранение заметки") {
            onView(withClassName(containsString(ImageButton::class.java.simpleName))).perform(click())
            deviceScreenshot("dialog_save_display")
            onView(allOf(withId(android.R.id.button1), isDisplayed())).perform(click())
        }
        step("Выбор заметки") {
            onView(withId(R.id.relativeLayout)).perform(longClick())
            deviceScreenshot("choose_note")
        }
        step("Проверка отображения диалогового окна об удалении") {
            onView(withContentDescription("Ещё")).perform(click())
            deviceScreenshot("dialog_delete_display")
        }
        step("Проверка_отсутствия_заметки") {
            onView(allOf(withId(android.R.id.button1), withText("ОК"), isDisplayed())).perform(click())
            onView(withId(R.id.titleView)).check(doesNotExist())
            onView(withId(R.id.bodyView)).check(doesNotExist())
            deviceScreenshot("no_note_on_main_page")
        }
    }
}
