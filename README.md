# lesbians.lol Server

This project powers the lesbians.lol server, which serves images extracted from MP4 video sources.

> Note: The application used to extract frames has not been committed to this repository.
> If you’re interested in the source code or binaries, feel free to contact me. Alternatively, you can use ffmpeg to extract frames.


The core idea is to control the number of extracted frames by adjusting the frame rate:
	•	Extracting at 60fps means capturing 60 frames per second — often excessive.
	•	A good starting point is 10–15fps, which balances quality and storage.

You can tweak both the frame rate and duration to control how many images are generated.

The server uses Jetty and serves images from the resources directory.

More projects will be open-sourced soon — stay tuned!
