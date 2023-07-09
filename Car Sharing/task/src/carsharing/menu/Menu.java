package carsharing.menu;

import java.util.*;

public class Menu {
    private static Menu active;
    private String title;
    private Map<String, String> options = new LinkedHashMap<>();
    private Map<String, MenuOptionSelectorInterface> optionsSelectorInterface = new HashMap<>();
    private Menu parent;
    private final List<Menu> subMenus = new ArrayList<>();

    public Menu() {

    }

    public Menu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void clearOptions() {
        this.options = new LinkedHashMap<>();
        this.optionsSelectorInterface = new HashMap<>();
    }

    public static Menu getActive() {
        return active;
    }

    public static void setActive(Menu active) {
        Menu.active = active;
    }

    public static void run(Menu menu, Scanner input) {
        String option;
        boolean exit = false;
        do {
            if (Menu.getActive().getTitle() != null) {
                System.out.println(Menu.getActive().getTitle() + ":");
            }
            active.display();
            option = input.nextLine();

            if (active.equals(menu) && option.equals("0")) {
                exit = true;
            }
            System.out.println();

            active.getOptionsSelector(option).select();
        } while (!exit);
    }

    public void display() {
        if (options != null) {
            options.forEach((key, value) -> System.out.printf("%s. %s\n", key, value));
        }
    }

    public void addOption(String optionKey, String optionLabel, MenuOptionSelectorInterface optionInterface) {
        this.setOptionLabel(optionKey, optionLabel);
        this.setOption(optionKey, optionInterface);
    }

    public void setOptionLabel(String optionKey, String optionLabel) {
        this.options.put(optionKey, optionLabel);
    }

    public void setOption(String optionKey, MenuOptionSelectorInterface optionInterface) {
        this.optionsSelectorInterface.put(optionKey, optionInterface);
    }

    public MenuOptionSelectorInterface getOptionsSelector(String optionKey) {
        return optionsSelectorInterface.get(optionKey);
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
        parent.subMenus.add(this);
    }

    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void addSubMenu(Menu subMenu){
        this.subMenus.add(subMenu);
        subMenu.parent = this;
    }

    public void removeSubMenu(Menu subMenu){
        this.subMenus.remove(subMenu);
        subMenu.parent = null;
    }
}
