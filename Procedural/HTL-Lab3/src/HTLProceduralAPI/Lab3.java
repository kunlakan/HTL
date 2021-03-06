package HTLProceduralAPI;

/**
 * @author Kunlakan (Jeen) Cherdchusilp
 * @author Tom Lai
 * 
 * ----------------------------------------------------------------------------
 * LAB 3 - If/Else: Draw Wizards with If/Else Statement
 * 
 * LEARNING OBJECTIVE:
 * 		Student will be able to demonstrate an understanding of conditionals
 * 
 * FUNCTIONS TO USE:
 * 		keyboardIsPressingRight()
 * 		keyboardIsPressingLeft()
 * 		keyboardIsPressingUp()
 * 		keyboardIsPressingDown()
 */

public class Lab3 extends HTLProceduralAPI
{
	public void updateGame()
	{
		if (keyboardIsPressingRight()) 
		{
			drawSpeedyWizard(19, 5); // x = 19, y = 5
		} 
		else if (keyboardIsPressingLeft()) 
		{
			drawSpeedyWizard(0, 5); // x = 0, y = 5
		} 
		else if (keyboardIsPressingUp())
		{
			drawMedicWizard(10, 9); // x = 10, y = 9
		} 
		else if (keyboardIsPressingDown()) 
		{
			drawMedicWizard(10, 0); // x = 10, y = 0
		}

	}
}
