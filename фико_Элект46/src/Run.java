
import java.math.BigDecimal;
import java.math.RoundingMode;

import windows.Main;

public class Run
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{		
		// TODO Auto-generated method stub
		Main win = new Main();
		win.setVisible(true);
		System.out.println(parseStringToDouble("4 517,4020"));
	}
	
	private static Double parseStringToDouble(String value)
	{
		if (value != null)
		{
			value = value.replace(" ", "");
			value = value.replace(",", ".");

			try
			{
				
				return new BigDecimal(value).setScale(4, RoundingMode.HALF_UP).doubleValue();
			}
			catch (Exception e)
			{
				return 0.0;
			}
		}

		return 0.0;
	}
}
