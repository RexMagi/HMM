package distributions;

public class MultivariateDistribution {
		Function[] distros;

		/**
		 * @return the distros
		 */
		public Function[] getDistros() {
			return distros;
		}

		/**
		 * @param distros the distros to set
		 */
		public void setDistros(Function[] distros) {
			this.distros = distros;
		}

		public MultivariateDistribution(Function[] distros) {
			this.distros = distros;
		}
		
		
}
