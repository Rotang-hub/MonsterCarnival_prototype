package io.github.rotanghub.monstercarnival.manager;

public class IncomeLevel
{
    Manager manager;

    int redIncomeLevel = 1;
    int blueIncomeLevel = 1;

    public IncomeLevel(Manager manager)
    {
        this.manager = manager;
    }

    public void upgradeLevel(String team)
    {
        if(team == "red")
        {
            if(redIncomeLevel < 6)
            {
                redIncomeLevel++;
            }
        }
        if(team == "blue")
        {

        }
    }

    public void reset()
    {
        redIncomeLevel = 1;
        blueIncomeLevel = 1;
    }
}
